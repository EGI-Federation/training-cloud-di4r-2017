# jOCCI-dump-model
This is a Java library to render <a href="http://occi-wg.org/about/specification/">Open Cloud Computing Interface (OCCI)</a> queries.
Detailed documentation is available in the project <a href="https://github.com/EGI-FCTF/jOCCI-api/wiki">wiki</a>.

## Compile and Run

Edit the source code in `src/main/java/it/infn/ct/Exercise1.java` to use your preferred VO and provider endpoint :
```
[..]
String OCCI_ENDPOINT_HOST = "https://carach5.ics.muni.cz:11443"; // <= Change here!
String VO = "training.egi.eu";                                   // <= Change here!
```

Compile and package with maven:
```
$ mvn compile && mvn package
```

Run (you may redirect the output to a file):
```
$ java –jar target/jocci-dump-model-1.0-jar-with-dependencies.jar
```


## Dependencies

jOCCI-dump-model uses:
- jocci-api (v0.2.5)
- slf4j-jdk14 (v1.7.12)

These are already included in the Maven pom.xml file and automatically downloaded when building.

You can also add them to your projects with:

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

## Requirements

- JDK 7+
- Maven

## Contribute
- Fork it
- Create a branch (git checkout -b my_markup)
- Commit your changes (git commit -am "My changes")
- Push to the branch (git push origin my_markup)
- Create an Issue with a link to your branch

<h2>License</h2>
Licensed under the Apache License, Version 2.0 (the "License"); you may not use this project except in compliance with the License. You may obtain a copy of the License at <a href="http://www.apache.org/licenses/LICENSE-2.0">http://www.apache.org/licenses/LICENSE-2.0</a>.

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
