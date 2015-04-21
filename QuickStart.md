# Quick start, protobuf-javame #

  1. [Download](http://code.google.com/p/protobuf-javame/downloads/list) the latest version add the protobuf-javame.jar to your project.
  1. Use the proto2javame.jar to generate the source code from your proto-file.<br />**Command:**`java -jar proto2javame.jar --java_out=<generated-source-folder> <path to your proto-file>`
  1. Use the generated builder object to read/write the populated object:<br />**Write command:** `generatedObject.writeTo(...)`<br />**Read command:** `MyGeneratedObject generatedObject = MyGeneratedObject.parseFrom(...)`
<br />
<br />
[User Documentation](http://code.google.com/p/protobuf-javame/wiki/UserDocumentation)