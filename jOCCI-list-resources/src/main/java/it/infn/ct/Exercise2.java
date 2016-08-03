
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

import java.util.List;
import java.util.Arrays;
import java.util.Properties;
import java.net.URI;

import cz.cesnet.cloud.occi.Model;
import cz.cesnet.cloud.occi.api.Client;
import cz.cesnet.cloud.occi.api.EntityBuilder;
import cz.cesnet.cloud.occi.api.exception.CommunicationException;
import cz.cesnet.cloud.occi.api.exception.EntityBuildingException;
import cz.cesnet.cloud.occi.api.http.HTTPClient;
import cz.cesnet.cloud.occi.api.http.auth.HTTPAuthentication;
import cz.cesnet.cloud.occi.api.http.auth.VOMSAuthentication;
import cz.cesnet.cloud.occi.core.Action;
import cz.cesnet.cloud.occi.core.Mixin;
import cz.cesnet.cloud.occi.core.Resource;
import cz.cesnet.cloud.occi.exception.AmbiguousIdentifierException;
import cz.cesnet.cloud.occi.exception.InvalidAttributeValueException;
import cz.cesnet.cloud.occi.exception.RenderingException;
import cz.cesnet.cloud.occi.parser.MediaType;

public class Exercise2
{
    // Listing cloud resources from provider.
    // Available resources that can be listed via API are the following:
    // - os_tpl = virtual appliances (aka VA) in the provider, 
    // - resource_tpl = template (aka flavour) resources, 
    // - compute = computing resources, 
    // - storage = storage resources,
    // - network = network resources.
    public static void doList (Properties properties, Model model, Client client)
    {
	System.out.println();
	
	try {
		if (properties.getProperty("RESOURCE").equals("compute")) 
		{
			List<URI> list = client.list("compute");
			System.out.println("[+] Listing active VMs in the given cloud resource");

        		for (URI uri : list) {
 				if (uri.toString().contains("compute")) 
                			System.out.println(uri);
		        }
		}
		
		if (properties.getProperty("RESOURCE").equals("os_tpl")) 
		{
			System.out.println("[+] Listing *os_tpl* resources");
			for (Mixin mixin : model.findRelatedMixins("os_tpl"))
                                System.out.println("- " + mixin.getTitle() + "\n"
                                + mixin.getScheme() + mixin.getTerm());
		}

                if (properties.getProperty("RESOURCE").equals("resource_tpl"))
                {
                        System.out.println("[+] Listing *resource_tpl* resources");
			for (Mixin mixin : model.findRelatedMixins("resource_tpl"))
                                System.out.println(mixin.getScheme() + mixin.getTerm());
                }

		if (properties.getProperty("RESOURCE").equals("storage"))
                {
			List<URI> list = client.list("storage");
                        System.out.println("[+] Listing *storage* resources");
			for (URI uri : list)
				if (uri.toString().contains("storage"))
	                                System.out.println(uri);
                }

		if (properties.getProperty("RESOURCE").equals("network"))
                {
			List<URI> list = client.list("network");
                        System.out.println("[+] Listing *network* resources");
                        for (URI uri : list)
                                if (uri.toString().contains("network"))
                                        System.out.println(uri);
                }
		
	} catch (CommunicationException | AmbiguousIdentifierException ex) {
		throw new RuntimeException(ex);
	}
    }

    public static void main (String[] args)
    {
	// [ Setting preferences here! ]
	String AUTH = "x509"; 
        //String OCCI_ENDPOINT_HOST = "https://carach5.ics.muni.cz:11443"; // <= Change here!
        String OCCI_ENDPOINT_HOST = "https://cloud.ifca.es:8787/occi1.1"; // <= Change here!
        String TRUSTED_CERT_REPOSITORY_PATH = "/etc/grid-security/certificates";
        String PROXY_PATH = "/tmp/x509up_u1000"; // <= Change here!
	Boolean verbose = true;

	String ACTION = "list"; // [ *Listing* ] available resources 

	// Possible resources are = os_tpl, resource_tpl, compute, storage and network
        List<String> RESOURCE = Arrays.asList("os_tpl"); // <= Change here!
	
	if (verbose) {
		System.out.println();
		if (ACTION != null && !ACTION.isEmpty()) 
			System.out.println("[ACTION] = " + ACTION);
		else	
			System.out.println("[ACTION] = Get dump model");
		System.out.println("AUTH = " + AUTH);
		if (OCCI_ENDPOINT_HOST != null && !OCCI_ENDPOINT_HOST.isEmpty()) 
			System.out.println("OCCI_ENDPOINT_HOST = " + OCCI_ENDPOINT_HOST);
		if (RESOURCE != null && !RESOURCE.isEmpty()) 
			System.out.println("RESOURCE = " + RESOURCE);
		if (TRUSTED_CERT_REPOSITORY_PATH != null && !TRUSTED_CERT_REPOSITORY_PATH.isEmpty()) 
			System.out.println("TRUSTED_CERT_REPOSITORY_PATH = " + TRUSTED_CERT_REPOSITORY_PATH);
		if (PROXY_PATH != null && !PROXY_PATH.isEmpty()) 
			System.out.println("PROXY_PATH = " + PROXY_PATH);
		if (verbose) System.out.println("Verbose = ON ");
		else System.out.println("Verbose = OFF ");
	}

        Properties properties = new Properties();
	if (ACTION != null && !ACTION.isEmpty())
	        properties.setProperty("ACTION", ACTION);
	if (OCCI_ENDPOINT_HOST != null && !OCCI_ENDPOINT_HOST.isEmpty())
	        properties.setProperty("OCCI_ENDPOINT_HOST", OCCI_ENDPOINT_HOST);

	if (RESOURCE != null && !RESOURCE.isEmpty()) 
        for (int i=0; i<RESOURCE.size(); i++) {
	        if ( (!RESOURCE.get(i).equals("compute")) && 
	             (!RESOURCE.get(i).equals("storage")) &&
	             (!RESOURCE.get(i).equals("network")) &&
	             (!RESOURCE.get(i).equals("os_tpl")) &&
	             (!RESOURCE.get(i).equals("resource_tpl")) )
        		properties.setProperty("OCCI_VM_RESOURCE_ID", RESOURCE.get(i));
        		
		else { 
			properties.setProperty("RESOURCE", RESOURCE.get(i));
        		properties.setProperty("OCCI_VM_RESOURCE_ID", "empty");
		}
        }
			
        properties.setProperty("TRUSTED_CERT_REPOSITORY_PATH", TRUSTED_CERT_REPOSITORY_PATH);
        properties.setProperty("PROXY_PATH", PROXY_PATH);
        properties.setProperty("OCCI_AUTH", AUTH);

	try {
		HTTPAuthentication authentication = new VOMSAuthentication(PROXY_PATH);
		
		authentication.setCAPath(TRUSTED_CERT_REPOSITORY_PATH);
	        
		Client client = new HTTPClient(URI.create(OCCI_ENDPOINT_HOST),
                                authentication, MediaType.TEXT_PLAIN, false);

            	//Connect client
            	client.connect();
		
		Model model = client.getModel();
                EntityBuilder eb = new EntityBuilder(model);

		if  (ACTION.equals("list")) 
			doList(properties, model, client);

	} catch (CommunicationException ex ) {
                 throw new RuntimeException(ex);
        }
    }
}
