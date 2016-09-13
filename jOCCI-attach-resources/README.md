# jOCCI-attach-resources
This is a Java library to render <a href="http://occi-wg.org/about/specification/">Open Cloud Computing Interface (OCCI)</a> queries.

Detailed documentation is available in the project <a href="https://github.com/EGI-FCTF/jOCCI-api/wiki">wiki</a>.

## Compile and Run

Access the maven project

```cd di4r-training/jOCCI-attach-resources/```

Edit your settings in the `src/main/java/it/infn/ct/Exercise6.java` source code:
```
[..]
String OCCI_ENDPOINT_HOST = "https://carach5.ics.muni.cz:11443"; // <= Change here!
String PROXY_PATH = "/tmp/x509up_u1000"; // <= Change here!

List<String> RESOURCE = Arrays.asList("compute",
    "https://carach5.ics.muni.cz:11443/compute/74479"); // <= Change here!
    String LINK_RESOURCE = ("https://carach5.ics.muni.cz:11443/storage/3918"); // <= Change here!
```

Compile and package with maven:
```
$ mvn compile && mvn package
```

Run (you may redirect the output to a file):
```
$ java –jar target/jocci-attach-resources-1.0-jar-with-dependencies.jar
```

## Dependencies

jOCCI-attch-resources uses:
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
