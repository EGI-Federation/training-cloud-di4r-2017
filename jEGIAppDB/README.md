# jEGIAppDB
jEGIAppDB is an open-source Java application to gather from the <a href="https://appdb.egi.eu/">EGI Application Database</a> the list of cloud providers that have subscribed a given VO. For each provider the available Virtual Appliances and resource template will be shown.

<h2>Requirements</h2>
jEGIAppDB uses:
- Jersey-client (v1.8)
- xml-apis (v2.0.2)

<h2>Install</h2>
If using Maven, add the following dependencies to your pom.xml file:

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

<h2>Contribute</h2>
- Fork it
- Create a branch (git checkout -b my_markup)
- Commit your changes (git commit -am "My changes")
- Push to the branch (git push origin my_markup)
- Create an Issue with a link to your branch

<h2>License</h2>
Licensed under the Apache License, Version 2.0 (the "License"); you may not use this project except in compliance with the License. You may obtain a copy of the License at <a href="http://www.apache.org/licenses/LICENSE-2.0">http://www.apache.org/licenses/LICENSE-2.0</a>.

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
