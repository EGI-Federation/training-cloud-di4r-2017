
/*
 *  Copyright 2016 EGI Foundation
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at

 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package it.infn.ct;

import java.io.StringReader;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.xml.sax.InputSource;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;


public class jEGIAppDB
{

    public static String endpoint = "appdb.egi.eu";
    public static String VO = "training.egi.eu"; 	// <= Change here!


    public static String get_OCCI_ID(String id)
    {
	String result = ""; 

	try {
		Client client = Client.create();

	        WebResource webResource = client
                    .resource("https://" + endpoint + "/rest/1.0/va_providers/" + id);
		
        	ClientResponse response = webResource.accept("application/json")
                    .get(ClientResponse.class);

	        if (response.getStatus() != 200)
        	        throw new RuntimeException("Failed : HTTP error code : "
                	        + response.getStatus());

	        String output = response.getEntity(String.class);

            	// Parsing XML reponse from server
	        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                InputSource is = new InputSource(new StringReader(output));
                Document doc = dBuilder.parse(is);
                doc.getDocumentElement().normalize();

		NodeList List = doc.getElementsByTagName("virtualization:provider");
            	for (int tmp = 0; tmp < List.getLength(); tmp++)
            	{
                	Node node = List.item(tmp);
			String occiendpoint = "";

	                if (node.getNodeType() == Node.ELEMENT_NODE) 
			{
                	        Element element = (Element) node;

				if (element.getElementsByTagName("provider:endpoint_url").getLength()>0)

	                        	occiendpoint = element.getElementsByTagName("provider:endpoint_url")
					.item(0).getTextContent();
				else
					occiendpoint = "N/A";

				result = occiendpoint;
			}
		}
	} catch (Exception e) { e.printStackTrace(); }
	
	return (result);
    }


    public static void get_resource(String id)
    {
	try {
		Client client = Client.create();

                WebResource webResource = client
                    .resource("https://" + endpoint + "/rest/1.0/va_providers/" + id);

                ClientResponse response = webResource.accept("application/json")
                    .get(ClientResponse.class);

                if (response.getStatus() != 200)
                        throw new RuntimeException("Failed : HTTP error code : "
                                + response.getStatus());

                String output = response.getEntity(String.class);

                // Parsing XML reponse from server
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                InputSource is = new InputSource(new StringReader(output));
                Document doc = dBuilder.parse(is);
                doc.getDocumentElement().normalize();

		NodeList List = doc.getElementsByTagName("provider:template");
                for (int tmp = 0; tmp < List.getLength(); tmp++)
                {
                        Node node = List.item(tmp);

                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                                Element element = (Element) node;
                                String template = element.getElementsByTagName("provider_template:resource_name")
                                        .item(0).getTextContent();

				if (template.startsWith("http://")) 
					//template=template.substring( template.indexOf("resource_tpl"), template.length() );
                                        template=template.substring( template.indexOf("#"), template.length() );

                                if (template.contains("resource_tpl")) System.out.println("\t" + template);
                                else System.out.println("\t resource_tpl" + template);
                        }
                }

	} catch (Exception e) { e.printStackTrace(); }
    }

    
    public static void get_VA(String id, String VO)
    {
        try {
                Client client = Client.create();

                WebResource webResource = client
                    .resource("https://" + endpoint + "/rest/1.0/va_providers/" + id);

                ClientResponse response = webResource.accept("application/json")
                    .get(ClientResponse.class);

                if (response.getStatus() != 200)
                        throw new RuntimeException("Failed : HTTP error code : "
                                + response.getStatus());

                String output = response.getEntity(String.class);

                // Parsing XML reponse from server
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                InputSource is = new InputSource(new StringReader(output));
                Document doc = dBuilder.parse(is);
                doc.getDocumentElement().normalize();

                NodeList List = doc.getElementsByTagName("provider:image");
                for (int tmp = 0; tmp < List.getLength(); tmp++)
                {
                        Node node = List.item(tmp);

                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                                Element element = (Element) node;
			
				String appName = element.getAttribute("appname");	
				String OCCI_ID = element.getAttribute("va_provider_image_id");
				String URI = element.getAttribute("mp_uri");
				String _vo = element.getAttribute("voname");
				String version = element.getAttribute("vmiversion");

				if (_vo.equals(VO)) {
	                                System.out.println("\n\t - Name = " + appName + " [v" + version + "]");
					if (OCCI_ID.startsWith("os_tpl")) {
                                                String _OCCI_ID = OCCI_ID.replace("os_tpl","os");
                                                System.out.println("\t - OCCI ID = http://schemas.openstack.org/template/" + _OCCI_ID);
                                        } else System.out.println("\t - OCCI ID = " + OCCI_ID);
                        	        System.out.println("\t - URI = " + URI);
				}
                        }
                }

        } catch (Exception e) { e.printStackTrace(); }
    }

    
    public static void get_provider_metadata(String sitenameID)
    {

        try {
                Client client = Client.create();

                WebResource webResource = client
                    .resource("https://" + endpoint + "/rest/1.0/va_providers/" + sitenameID);

                ClientResponse response = webResource.accept("application/json")
                    .get(ClientResponse.class);

                if (response.getStatus() != 200)
                        throw new RuntimeException("Failed : HTTP error code : "
                                + response.getStatus());

                String output = response.getEntity(String.class);

                // Parsing XML reponse from server
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                InputSource is = new InputSource(new StringReader(output));
                Document doc = dBuilder.parse(is);
                doc.getDocumentElement().normalize();

                NodeList List = doc.getElementsByTagName("appdb:site");
                for (int tmp = 0; tmp < List.getLength(); tmp++)
                {
                        Node node = List.item(tmp);
                        if (node.getNodeType() == Node.ELEMENT_NODE)
                        {
                                Element element = (Element) node;

                                // Get the EGI Cloud Provider name 
                                String provider = element.getAttribute("name");

				// Get the official sitename
                                String sitename = element
                                        .getElementsByTagName("site:officialname")
                                        .item(0).getTextContent();

                                // Get the status 
                                String status = element.getAttribute("status");

                                // Get the URL of the provider in the EGI Application Database
                                String url =  "https://" + endpoint + "/rest/1.0/va_providers/" + sitenameID;

                                System.out.println("\n- " + provider + " [" + sitenameID + "] ");
				System.out.println("\t--> Sitename: " + sitename);
                                System.out.println("\t--> Endpoint: " + get_OCCI_ID(sitenameID));
                                System.out.println("\t--> Status: " + status);
                                System.out.println("\t--> URL: " + url);
                        }
                }


        } catch (Exception e) { e.printStackTrace(); }

    }



    public static void main (String[] args)
    {
        try {
            
	    Client client = Client.create();

	    WebResource webResource = client
                    .resource("https://" + endpoint + "/rest/1.0/sites?flt=vo.name:" + VO + "&site.supports:1");

            ClientResponse response = webResource.accept("application/json")
                    .get(ClientResponse.class);

            if (response.getStatus() != 200) 
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());

            String output = response.getEntity(String.class);

	    // Parsing XML reponse from server
	    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(output));
            Document doc = dBuilder.parse(is);
	    doc.getDocumentElement().normalize();

	    System.out.println("\n ~ Listing providers that have subscribed the [" + VO + "] VO ");

	    NodeList Site = doc.getElementsByTagName("site:service");
	    for (int k = 0; k < Site.getLength(); k++) 
	    {
		Node site = Site.item(k);

		if (site.getNodeType() == Node.ELEMENT_NODE) {

			Element _element = (Element) site;

			// Get the site attributes
                        String sitenameID =  _element.getAttribute("id");

                        // Get provider metadata
                        get_provider_metadata(sitenameID);

                        // Get available resource(s) template
                        System.out.println("\n ~ Listing available resource(s) templates \n");
                        get_resource(sitenameID);

			// Get available VA images
                        System.out.println("\n ~ Listing available Virtual Appliance(s) ");
                        get_VA(sitenameID, VO);

		}
	    }

        } catch (Exception e) { e.printStackTrace(); }
	
	System.out.println();
    }
}
