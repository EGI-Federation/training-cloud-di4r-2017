# jEGIAppDB
jEGIAppDB is an open-source Java application to gather from the [EGI Application Database](https://appdb.egi.eu/)
the list of cloud providers that have subscribed a given VO. 
For each provider the available Virtual Appliances and resource templates will be shown.

## Compile and Run

Access the maven project

```cd di4r-training/jEGIAppDB/```

Edit the source code in `src/main/java/it/infn/ct/jEGIAppDB.java` to use your preferred VO (*):
```
[..]
String VO = "training.egi.eu"; // <= Change here!
```

N.B.: For this training event we will use training.egi.eu VO.

Compile and package with maven:
```
$ mvn compile && mvn package
```

Run (you may redirect the output to a file):
```
$ java –jar target/jEGIAppDB-1.0-jar-with-dependencies.jar
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
