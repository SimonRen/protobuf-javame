User documentation for the protobuf-javame implementation.

# Introduction #
protobuf-javame is an implementation of the Google Protocol Buffers project that supports Java ME. The implementation is created to be compatible with the standard Google Protocol Buffer project. This means you can generate source code from the same proto-file with the standard implementation and the Java ME implementation and use them together. For example the Java ME version can be used in the client and the standard implementation used at the server.

The Java ME implementation is created to be small and lightweight. The architecture and design of the Java ME implementation is very different from the standard version. It does, however, use almost the same low level classes and business logic as the standard implementation for creating the actual output bytes. These classes have been adapted to the Java ME environment. Some of the features in the standard implementations have not been implemented in the Java ME version. This is done to make sure the implementation remains small and fast.

If you are missing some features and maybe you even have an idea how they can be implemented, do not hesitate to send me an e-mail (hansjar at gmail dot com) and I am happy to discuss the issues with you.


---

# Packages #
The application consists of two jar files:
  1. proto2javame-`[version]`.jar, the code generator application. It takes two arguments. --java\_out points to the directory where the generated code will be stored. The final argument is the location to the proto-file that will be use to generate the code
  1. protobuf-javame-`[version]`.jar, the library that must be added to your application. The generated code has dependencies to this library. The library should not be called directly from the application code, everything should be done through the generated Java-object.


---

# Features #
|-Proto type-|-Java type-|
|:-----------|:----------|
|string|String|
|int32|int|
|int64|long|
|double|double|
|float|float|
|bool|boolean|
|bytes|ByteString|
|enum|int|
_Supported data types_

The project has its own code generator written completely in Java. This will generate Java code that is meant to run on Java ME enabled mobile devices from proto-files. All you have to do is download the proto2javame jar-file and run the following command to generate the source code: `java -jar proto2javame.jar --java_out=<generated source folder> <path to proto-file>`. It supports the required, repeated and optional fields.

**Required** means the field must be set before the object is sent across the network. The check if all required fields are added is not run before runtime.

**Repeated** adds a list, meaning if you write “repeated string names = 1;” the code generated will contain a Vector that is called names. You can either add single element to the Vector object created by the generated class or send in your own list. There are no requirement that the list contains any elements before it is sent.

**Optional** is a single field that does not have to be set before the object is sent. If it is not set the generated code will simply skip sending it.
<br />
## Nested types ##
Version 1.0.0 added support for nested types. It is done in a slightly different way than the standard protobuf implementation. You need to include your nested types in the same proto-file.<br /><br />**Example:**<br />
```
package net.jarlehansen.protobuf.javame;

option java_package = "net.jarlehansen.protobuf.javame.examples.generated";
option java_outer_classname = "ExampleNestedObjectImpl";

message ExampleNestedObject {
	required Number number = 1;
}

message Number {
	optional int32 id = 1;
}
```


Each nested message will create its own class, it is not nested into one wrapper class like in the standard protobuf implementation. All nested classes will be generated in the same output folder with the same package-declaration. You can use all scopes on nested class (optional, required and repeated).
<br />
## Recursion ##
Supported in version 1.1.0
<br /><br />**Example:**<br />
```
message ProtoCategory{
    required int32 id = 1;
    required string name = 2;
    repeated ProtoCategory category = 3;
}
```

Following is the generated code :
```
case fieldNumberCategory:
    Vector vcCategory = reader.readMessages(fieldNumberCategory);
    for(int i = 0 ; i < vcCategory.size(); i++) {
	byte[] eachBinData = (byte[]) vcCategory.elementAt(i);
	ProtoCategory.Builder builderCategory = ProtoCategory.newBuilder();
	InputReader innerInputReader = new InputReader(eachBinData, unknownTagHandler);
	boolean boolCategory = true;
	int nestedFieldCategory = -1;
	while(boolCategory) {
	    nestedFieldCategory = getNextFieldNumber(innerInputReader);
	    boolCategory = ProtoCategory.populateBuilderWithField(innerInputReader, builderCategory, nestedFieldCategory);
	}
	eachBinData = null;
	innerInputReader = null;
        builder.addElementCategory(builderCategory.build());				       
    }
    ...
```

Note: Recursive field (eg. ProtoCategory ) should be put in the last position, otherwise serialization will be screwed up.
<br />
## Enum types ##
Also added in version 1.0.0 is support for enums. This is not Java 1.5 style enums since these are not supported by Java ME. The generated code will simply contain a inner class with the created enum that consists of static final int variables.
<br /><br />**Example:**<br />
```
package net.jarlehansen.protobuf.javame;

option java_package = "net.jarlehansen.protobuf.javame.examples.generated";
option java_outer_classname = "ExampleEnumObjectImpl";

message ExampleEnumObject {
	enum Color {
		RED = 1;
		BLUE = 2;
	}
	
	repeated Color color = 1;
}
```
<br />
This will create the following code:<br />
```
public static class Color {
		public static int RED = 1;
		public static int BLUE = 2;
```

<br />
Also generated is a method that makes it easy for you to get the name of the enum, in this case all I would have to do is `Color.getStringValue(Color.RED);`
<br />This will return "RED" as a String.
## Unknown fields handling ##
There are cases where the proto-file is updated, but you still have to support older versions of the contract. In protobuf-javame it is possible to register an `UnknownTagHandler` to be able to register all received tags that are unknown. All you have to do is to register an Object implementing the `UnknownTagHandler` interface on your generated Java class, and your object will be called each time an unknown field is read.<br /><br />**UnknownTagHandler interface:**<br />
```
public interface UnknownTagHandler {
	public void unknownTag(final String unknownTag);
}
```
<br />**setUnknownTagHandler method on the generated class:**<br />
```
ExampleObject.setUnknownTagHandler(myUnknownTagHandler);
```


---

# Differences, standard implementation and Java ME #
Since this project is created for Java ME it is naturally more limited than the Java SE version.  This section will point out the differences between the standard Google Protocol Buffer and the Java ME implementation. It will highlight the features that are available in the standard implementation, but not supported by the Java ME version.

**Some of the missing features:**
  * **Import definitions** (proto-file), the nested types must be declared in the same proto-file.
  * **java\_outer\_classnames** (in proto-file), The class name for the generated source code will always be the message name. There is no need for the java\_outer\_classname. If you include the java\_outer\_classname option in the proto-file this is simply ignored when generating the source code.
  * **package** (in proto-file), the package is not used and is, like the java\_outer\_class, ignored when generating code from the proto-file.
  * **extensions**
  * **default values**



---

# Example #
**Proto-file:**<br />
```
package net.jarlehansen.protobuf.javame;

option java_package = "net.jarlehansen.protobuf.javame.examples.generated";
option java_outer_classname = "ExampleObjectImpl";

message ExampleObject {
	required int32 id = 1;
	required string name = 2;
	optional string address = 3;
}
```
<br />**Using the generated Java-class:**<br />
```
public class SimpleExample {
	public static void main(String[] main) throws IOException {
		// Create a byte array from the ExampleObject instance
		final ExampleObject exampleObject = ExampleObject.newBuilder().setId(241).setName("Jarle").setAddress("Oslo").build();
		final byte[] data = exampleObject.toByteArray();
		
		// Transform the byte array back to a new ExampleObject instance with the same content
		final ExampleObject exampleObject2 = ExampleObject.parseFrom(data);
		System.out.println(exampleObject2.toString());
	}
}
```