This project is a Java ME implementation of Google Protocol buffer. It contains a code generator and a runtime library.
<br /><br />
**Main features:**
  * Compatible with the Google Protocol Buffer implementation.
  * Supports nested and enum types.
  * Has its own code generator (proto2javame) written in Java. The generated code is easy to read and change.
  * Possible to register your own handler for Unknown fields.
  * Small runtime library.
  * Most classes in the protobuf-javame library are public and non-final, this means you can easily use the API directly from your own code.
  * Tested against the 2.3.0 protobuf implementation.
<br />
_Any bug reports, questions, comments and suggestions can be sent to: **hansjar at gmail dot com**_
  * [Quick Start](http://code.google.com/p/protobuf-javame/wiki/QuickStart)
  * [User Documentation](http://code.google.com/p/protobuf-javame/wiki/UserDocumentation)


---

# News #

## April 4, 2012 ##
[Issue 9](http://code.google.com/p/protobuf-javame/issues/detail?id=9) fixed in version 1.1.2

## June 15, 2011 ##
Added support for comments in the proto-files.

## February 23, 2011 ##
Version 1.1.0 released. Added support for using repeated (lists) on custom types.
For more information go to the [user documentation](http://code.google.com/p/protobuf-javame/wiki/UserDocumentation), Recursion section.

## October 8, 2010 ##
Proto2javame + protobuf-javame version 1.0.1 released. This is a bug-fix release, fixing a problem with nested messages.

## September 5, 2010 ##
Documentation is updated and I have added a Quick start guide.

## September 5, 2010 ##
Proto2javame + protobuf-javame 1.0.0 released. Lots of updates, including support for nested messages and enums. The user documentation is not updated yet, but there are examples included in the zip-file. Updates to the documentation is coming soon!
Please try it out and report if any bugs are detected.
<br /><br />

---

## August 20, 2009 ##
Proto2javame 0.8.1 released. A small update fixing a problem with the parseFields-method in the generated source code.

## August 14, 2009 ##
Protobuf-javame 0.8.0 is released. If you have generated code with a previous version, please re-generate with 0.8.0 and update the runtime library.


**protobuf-javame:**
  * Added support for read and write delimited. This means messages will write the size of the proto message before sending it.
  * Added AbstractOutputWriter, this contains a few common writeTo methods and the createByteArray method. This will reduce the code that needs to be generated.
  * New example (FileExample in the protobuf-javame-examples project included in the protobuf-javame-0.8.0-all.zip file). This shows how to use writeTo, writeDelimitedTo, readFrom and readDelimitedFrom.

**proto2javame:**
  * Fixed a bug where primitives were added directly to the Vector list. This would create a compilation error. They are now first created as object and then added to the Vector.

## July 15, 2009 ##
A small update to the packaged zip file. This now includes a midlet example that uses Microlog to test out the Java ME protocol buffer. The zip-file is at version 0.7.1, while the other libraries are still 0.7.0.

If you need a logging library for Java ME I highly recommend [Microlog](http://microlog.sourceforge.net/snapshot/). Look at the new midlet example (protobuf-javame-midlet-example.zip packaged in the protobuf-javame-0.7.1-all.zip file) to see how it can be integrated easily with protobuf-javame.



## July 2, 2009 ##
Version 0.7.0 of the proto2javame and protobuf-javame implementation released.


**proto2javame**
  * Updated error message to list out the input when too many input arguments are sent in.
  * Supports backward compatibility of proto-files. This means you can update the proto-file, add new fields for example, and the old implementations will still be supported. Although the new fields will of course not be available on the old implementation.
**protobuf-javame**
  * Removed a lot of unused code and limited the scope of classes and methods.
  * Added UnknownTagHandler that makes it possible for users to retrieve the unknown fields. This happens if the proto-files are updated with new files. The UnknownTagHandler will be able to list the field number and general type of the new fields. Also the content of the field is returned.
  * The structure of the project has been refactored and updated.
  * The protobuf-javame-0.7.0-all.zip file now contains an example directory with two examples of using the protobuf-javame library.

## June 16, 2009 ##
0.6.0 of the proto2javame and protobuf-javame implementation released.

New proto2javame features:
  * Line separator is now determined from the line.separator system property.
  * Generated source code uses new package structure in the runtime library (needs protobuf-javame version 0.6.0 or newer to work).
  * Generated source code is more compact and should be easier to read.
  * Generated source code now contains the time and date when it was generated.
  * Added new integration tests to ensure that the generated files work with the standard Google Protocol Buffer implementation.

New protobuf-javame features:
  * Updated package and project structure.

## May 27, 2009 ##
Added user documentation.
## May 20, 2009 ##
0.5.0 version of protobuf-javame and proto2javame is released.
This is a project that makes it possible to use Google Protocol Buffers in Java ME. It comes with its own code generator (proto2javame) and runtime library (protobuf-javame). Since it is a Java ME version it is naturally more limited than the standard implementation (http://code.google.com/p/protobuf/).

It currently supports:
  * string
  * int (int32)
  * long (int64)
  * float
  * bytes (ByteString)
  * bool (boolean)
  * double

It also supports required, repeated and optional.
It uses the same proto-files as the standard implementation, but the source code files must be generated with proto2javame if you plan to use it in your Java ME code.

This is only version 0.5.0, so there are several planned features that will be added in the near future. However, if you are interested in this project and would like to use it please try it out and send me an e-mail on what you like, didn't like, suggestions to new features and bug reports.
Documentation and examples will be posted soon.


To use the project simply use the code generator to create java code from your .proto-file:<br />
`java -jar proto2javame.jar --java_out=src test.proto`

Finally, add the protobuf-javame.jar file to your project.
<br /><br /><br />
Any question, comments and suggestions can be sent to: **hansjar at gmail dot com**