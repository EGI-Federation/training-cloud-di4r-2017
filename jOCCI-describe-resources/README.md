# jOCCI-describe-resources
This is a Java library to render <a href="http://occi-wg.org/about/specification/">Open Cloud Computing Interface (OCCI)</a> queries.

Detailed documentation is available in the project <a href="https://github.com/EGI-FCTF/jOCCI-api/wiki">wiki</a>.

## Compile and Run

Edit your settings in the `src/main/java/it/infn/ct/Exercise3.java` source code:
```
[..]
String OCCI_ENDPOINT_HOST = "https://carach5.ics.muni.cz:11443"; // <= Change here!
String PROXY_PATH = "/tmp/x509up_u1000"; // <= Change here!

// [ *Describing* available resources (e.g. os_tpl, resource_tpl, compute, storage and network) ]
List<String> RESOURCE = Arrays.asList("compute",
        "https://carach5.ics.muni.cz:11443/compute/74374"); // <= Change here!
```

Compile and package with maven:
```
$ mvn compile && mvn package
```

Run (you may redirect the output to a file):
```
$ java –jar target/jocci-describe-resources-1.0-jar-with-dependencies.jar
```

## Dependencies

jEGIAppDB uses:
- Jersey-client (v1.8)
- xml-apis (v2.0.2)

These are already included in the Maven pom.xml file and automatically downloaded when building.

You can also add them to your projects with:

```
    <dependency>
        <groupId>com.sun.jersey</groupId>
        <artifactId>jersey-client</artifactId>
        <version>1.8</version>
    </dependency>

    <dependency>
        <groupId>xml-apis</groupId>
        <artifactId>xml-apis</artifactId>
        <version>2.0.2</version>
    </dependency>
```
