# jOCCI-delete-resources
This is a Java library to render <a href="http://occi-wg.org/about/specification/">Open Cloud Computing Interface (OCCI)</a> queries.

Detailed documentation is available in the project <a href="https://github.com/EGI-FCTF/jOCCI-api/wiki">wiki</a>.

## Compile and Run

Edit your settings in the `src/main/java/it/infn/ct/Exercise5.java` source code to delete a ```compute``` resource:
```
[..]
String OCCI_ENDPOINT_HOST = "https://carach5.ics.muni.cz:11443"; // <= Change here!
String PROXY_PATH = "/tmp/x509up_u1000"; // <= Change here!

// - Deleting running VM
List<String> RESOURCE = Arrays.asList("compute",
    "https://carach5.ics.muni.cz:11443/compute/74479"); // <= Change here!
```

Edit your settings in the `src/main/java/it/infn/ct/Exercise5.java` source code to delete a ```storage``` resource:
```
[..]
String OCCI_ENDPOINT_HOST = "https://carach5.ics.muni.cz:11443"; // <= Change here!
String PROXY_PATH = "/tmp/x509up_u1000"; // <= Change here!

// - Deleting block storage
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

## Dependencies

jOCCI-delete-resources uses:
- jocci-api (v0.2.5)
- slf4j-jdk14 (v1.7.12)

These are already included in the Maven pom.xml file and automatically downloaded when building.

You can also add them to your projects with:

```
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
```
