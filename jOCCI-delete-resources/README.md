# jOCCI-delete-resources
This is a Java library to render <a href="http://occi-wg.org/about/specification/">Open Cloud Computing Interface (OCCI)</a> queries.

Detailed documentation is available in the project <a href="https://github.com/EGI-FCTF/jOCCI-api/wiki">wiki</a>.

## Compile and Run

Edit your settings in the `src/main/java/it/infn/ct/Exercise5.java` source code to delete a ```compute``` resource:
```
[..]
String OCCI_ENDPOINT_HOST = "https://carach5.ics.muni.cz:11443"; // <= Change here!
String PROXY_PATH = "/tmp/x509up_u1000"; // <= Change here!

List<String> RESOURCE = Arrays.asList("compute",
    "https://carach5.ics.muni.cz:11443/compute/74479"); // <= Change here!
```

Edit your settings in the `src/main/java/it/infn/ct/Exercise5.java` source code to delete a ```storage``` resource:
```
[..]
String OCCI_ENDPOINT_HOST = "https://carach5.ics.muni.cz:11443"; // <= Change here!
String PROXY_PATH = "/tmp/x509up_u1000"; // <= Change here!

List<String> RESOURCE = Arrays.asList("storage",
    "https://carach5.ics.muni.cz:11443/compute/3918"); // <= Change here!
```

Compile and package with maven:
```
$ mvn compile && mvn package
```

Run (you may redirect the output to a file):
```
$ java –jar target/jocci-delete-resources-1.0-jar-with-dependencies.jar
```

<h2>Requirements</h2>
- JDK 7+
- Maven

<h2>Install</h2>
If using Maven, add the following dependencies to your pom.xml file:

    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-jdk14</artifactId>
        <version>1.7.12</version>
    </dependency>

    <dependency>
        <groupId>cz.cesnet.cloud</groupId>
        <artifactId>jocci-api</artifactId>
        <version>0.2.5</version>
        <scope>compile</scope>
    </dependency>
