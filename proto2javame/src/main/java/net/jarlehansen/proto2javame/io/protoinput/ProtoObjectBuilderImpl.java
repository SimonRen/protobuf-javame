package net.jarlehansen.proto2javame.io.protoinput;

import com.google.inject.Inject;
import net.jarlehansen.proto2javame.domain.proto.EnumData;
import net.jarlehansen.proto2javame.domain.proto.FieldData;
import net.jarlehansen.proto2javame.domain.proto.ProtoFileInput;
import net.jarlehansen.proto2javame.domain.proto.ValidTypes;
import net.jarlehansen.proto2javame.io.exception.ProtoFileValidationException;
import net.jarlehansen.proto2javame.io.protoinput.factory.ProtoParserFactory;
import net.jarlehansen.proto2javame.io.protoinput.util.ProtoTagUtil;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author hansjar@gmail.com Jarle Hansen
 */
public final class ProtoObjectBuilderImpl implements ProtoObjectBuilder {
    private final ProtoParserFactory protoParserFactory;

    private int lineNumber = 1;

    private final List<Integer> idList = new ArrayList<Integer>();
    private final List<ProtoFileInput> protoInputList = new ArrayList<ProtoFileInput>();
    private ProtoFileInput currentProtoInput = new ProtoFileInput();
    private String commonClassPackage = null;

    @Inject
    public ProtoObjectBuilderImpl(final ProtoParserFactory protoParserFactory) {
        this.protoParserFactory = protoParserFactory;
    }

    /**
     * Validates and save data loaded from the .proto file. Reads each line in
     * the file, and calls parseLine().
     *
     * @return ProtoFileInput
     */
    public List<ProtoFileInput> validateAndSaveProtoData(final String protoLocation) {
        ProtoTagUtil.isValidProtoFile(protoLocation);

        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(protoLocation));
            String line = reader.readLine();

            while (line != null) {
                line = removeComment(line);
                parseLine(line);

                line = reader.readLine();

                lineNumber++;
            }
        } catch (FileNotFoundException fnfe) {
            throw new ProtoFileValidationException("FileNotFoundException thrown in " + this.getClass().getName()
                    + ", this should never happen", fnfe);
        } catch (IOException io) {
            throw new ProtoFileValidationException("IOException thrown in " + this.getClass().getName()
                    + ", this should never happen", io);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException io2) {
                // No serious problem if this fails
            }
        }

        if(!protoParserFactory.getHasMessageEnd()) {
            ProtoFileValidationException pfve = new ProtoFileValidationException("The proto-file does not end with }");
            pfve.setLineNumber(lineNumber);
            throw pfve;
        }

        validateFieldIds();

        return protoInputList;
    }

    private void validateFieldIds() {
        for(ProtoFileInput protoInput : protoInputList) {
            final List<Integer> idList = new ArrayList<Integer>();

            for(FieldData fieldData : protoInput.getFields()) {
                int fieldId = fieldData.getId();

                if(idList.contains(fieldId)) {
                    throw new ProtoFileValidationException("Message field id must be unique, field: " + fieldData.getName());
                } else {
                    idList.add(fieldId);
                }
            }
        }
    }

    private String removeComment(final String line) {

        String uncommentedLine;
        if(line.contains("//")) {
            uncommentedLine = line.substring(0, (line.indexOf("//")));
        } else {
            uncommentedLine = line;
        }

        uncommentedLine = removeTrailingWhitespaces(uncommentedLine);

        return uncommentedLine;
    }

    private String removeTrailingWhitespaces(String uncommentedLine) {
        while(uncommentedLine.endsWith(" ")) {
           uncommentedLine = uncommentedLine.substring(0, uncommentedLine.length() - 1);
       }

        return uncommentedLine;
    }

    private void parseLine(final String line) {
        try {
            ProtoParser protoParser = protoParserFactory.getProtoParser(line);
            protoParser.parseAndAddProtoFile(currentProtoInput);
            
            if(protoParserFactory.isNewProto()) {
                if(commonClassPackage == null) {
                    commonClassPackage = currentProtoInput.getPackageName();
                }

                protoInputList.add(currentProtoInput);
                currentProtoInput = new ProtoFileInput();
                currentProtoInput.setPackageName(commonClassPackage);
            }
        } catch(ProtoFileValidationException pfve) {
            pfve.setLineNumber(lineNumber);
            throw pfve;
        }
    }
}
