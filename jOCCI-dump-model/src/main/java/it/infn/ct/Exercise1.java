
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

import java.util.Set;
import java.util.List;
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
import cz.cesnet.cloud.occi.core.ActionInstance;
import cz.cesnet.cloud.occi.core.Attribute;
import cz.cesnet.cloud.occi.core.Entity;
import cz.cesnet.cloud.occi.core.Mixin;
import cz.cesnet.cloud.occi.core.Resource;
import cz.cesnet.cloud.occi.core.Kind;
import cz.cesnet.cloud.occi.exception.AmbiguousIdentifierException;
import cz.cesnet.cloud.occi.parser.MediaType;

import org.apache.commons.codec.binary.Base64;

public class Exercise1
{
    // Get the dump of the available model(s) from
    // the EGI Cloud Provider
    public static void getModules (Properties properties, EntityBuilder eb, Model model, Client client)
    {
	try {
		// Dumping @kinds
		Set<Kind> kinds = model.getKinds();
	        System.out.println("\nDumping \"@kinds\":");

        	for (Kind kind : kinds) 
			System.out.println(kind.toString());

		// Dumping @mixins
		List<Mixin> mixins = model.findRelatedMixins("os_tpl");
	        System.out.println("\nDumping \"@mixins\":");

        	Resource compute = eb.getResource("resource");
	        System.out.println("\nMixin: os_tpl");
        	for (Mixin mixin : mixins) {
     			System.out.println();
			System.out.println(mixin.toText());
    		}

		mixins = model.findRelatedMixins("resource_tpl");
            	compute = eb.getResource("resource");
            	System.out.println("\nMixin: resource_tpl");
            	for (Mixin mixin : mixins) {
                	    System.out.println();
	                    System.out.println(mixin.toText());
            	}

		// Dumping @actions
		Set<Action> actions = model.getActions();
            	System.out.println("\nDumping \"@actions\":");
            	for (Action action : actions) {
                	System.out.println();
                    	System.out.println(action.getTerm() + ";"
                                + "scheme=\"" + action.getScheme() + "\";"
                                + "class=\"action\";"
                                + "title=\"" + action.getTitle() + "\"");
            	}

		// Dumping @resources
		List<URI> list = client.list("compute");
            	System.out.println("\nDumping \"@resources\":");
            	for (URI uri : list) {
                	if (uri.toString().contains("compute"))
                        	System.out.println(uri);
            	}

	} catch (AmbiguousIdentifierException | EntityBuildingException | CommunicationException ex ) {
            throw new RuntimeException(ex);
        }
    }

    public static void main (String[] args)
    {
	// [ Setting default preferences ]
	String AUTH = "x509"; 
        //String OCCI_ENDPOINT_HOST = "https://carach5.ics.muni.cz:11443"; // <= Change here!
        String OCCI_ENDPOINT_HOST = "https://cloud.ifca.es:8787/occi1.1"; // <= Change here!

        String TRUSTED_CERT_REPOSITORY_PATH = "/etc/grid-security/certificates";
        String PROXY_PATH = "/tmp/x509up_u1000"; // <= Change here!
	Boolean verbose = true;

	// [ Show available dump models for the host ]
	String ACTION = "";
	
	if (verbose) {
		System.out.println();
		if (ACTION != null && !ACTION.isEmpty()) 
			System.out.println("[ACTION] = " + ACTION);
		else	
			System.out.println("[ACTION] = Get available models");
		System.out.println("AUTH = " + AUTH);
		if (OCCI_ENDPOINT_HOST != null && !OCCI_ENDPOINT_HOST.isEmpty()) 
			System.out.println("OCCI_ENDPOINT_HOST = " + OCCI_ENDPOINT_HOST);
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

		// Implementing the different ACTIONS
		getModules(properties, eb, model, client);

	} catch (CommunicationException ex ) {
                 throw new RuntimeException(ex);
        }
    }
}
