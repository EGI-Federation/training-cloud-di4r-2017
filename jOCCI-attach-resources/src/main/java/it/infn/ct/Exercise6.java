
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
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Properties;

import java.net.URI;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.net.URISyntaxException;

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
import cz.cesnet.cloud.occi.core.Resource;
import cz.cesnet.cloud.occi.core.Kind;
import cz.cesnet.cloud.occi.exception.AmbiguousIdentifierException;
import cz.cesnet.cloud.occi.exception.InvalidAttributeValueException;
import cz.cesnet.cloud.occi.exception.RenderingException;
import cz.cesnet.cloud.occi.parser.MediaType;
import cz.cesnet.cloud.occi.core.Link;
import cz.cesnet.cloud.occi.infrastructure.NetworkInterface;
import cz.cesnet.cloud.occi.infrastructure.IPNetworkInterface;
import cz.cesnet.cloud.occi.infrastructure.enumeration.ComputeState;
import cz.cesnet.cloud.occi.infrastructure.Storage;
import cz.cesnet.cloud.occi.infrastructure.StorageLink;

public class Exercise6
{

    public static boolean testIpAddress(byte[] testAddress)
    {
        Inet4Address inet4Address;
        boolean result = false;

        try
        {
                inet4Address = (Inet4Address) InetAddress.getByAddress(testAddress);
                result = inet4Address.isSiteLocalAddress();
        } catch (UnknownHostException ex) { System.err.println(ex); }

        return result;
    }

    /* Check if the IP is public or not */
    public static String checkIP (String _ip)
    {
        String publicIP = null;
        String tmp  = "";
        int k=0;
        boolean check = false;

        Pattern patternID =
                Pattern.compile("^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$");

        Matcher matcher = patternID.matcher(_ip);
        while (matcher.find())
        {
                String _IP0 = matcher.group(1).replace(".","");
                String _IP1 = matcher.group(2).replace(".","");
                String _IP2 = matcher.group(3).replace(".","");
                String _IP3 = matcher.group(4).replace(".","");

                //CHECK if IP[k] is PRIVATE or PUBLIC
                byte[] rawAddress = {
                      (byte) Integer.parseInt(_IP0),
                      (byte) Integer.parseInt(_IP1),
                      (byte) Integer.parseInt(_IP2),
                      (byte) Integer.parseInt(_IP3)
                };

                if (!testIpAddress(rawAddress)) {
                    // Saving the public IP
                    publicIP = tmp;
                    check = true;
                }

                k++;
        } // while

        return publicIP;
    }

    // Linking cloud resources to running VMs.
    // Available resources that can be attached via API are the following:
    // - storage = storage resources,
    // - network = public network

    public static void doLink (Properties properties, EntityBuilder eb, Client client, Model model)
    {
	String networkInterfaceLocation = "";
	String public_networkInterface = "";
        String networkInterfaceLocation_stripped = "";
        Resource vm_resource = null;

	System.out.println();

	try {
		if (properties.getProperty("LINK_RESOURCE").contains("network")) 
		{
			System.out.println("[+] Link a public network interface to the VA");
			System.out.println(properties.getProperty("OCCI_RESOURCE_ID"));

			URI location = new URI (properties.getProperty("OCCI_RESOURCE_ID"));

			List<Entity> entities = client.describe(URI.create(properties.getProperty("OCCI_RESOURCE_ID")));
			//System.out.println(entities.get(0).toText());

			String IP = "";
			for (Entity entity : entities) {
				Resource resource = (Resource) entity;
                	        vm_resource = resource;
                        	Set<Link> links = resource.getLinks(NetworkInterface.TERM_DEFAULT);
				for (Link link : links) {
        	                    IP = link.getValue(IPNetworkInterface.ADDRESS_ATTRIBUTE_NAME);
                	            if (checkIP(IP) == null) {
                        	        networkInterfaceLocation = link.getKind().getLocation() + link.getId();
					//removing duplicate "/network/interface/" from URI
					networkInterfaceLocation_stripped =
        	                                networkInterfaceLocation.replace("/network/interface/","");
                	                networkInterfaceLocation = "/network/interface/" + networkInterfaceLocation_stripped;
					//System.out.println(public_networkInterface);
				    } 
				}
			}

			// Find a network resource that provides public IPs
			String public_network = "";
			List<URI> uris = client.list("network");
        		if (!uris.isEmpty()) {
        			// Listing networks
		        	for (URI uri : uris) {
					if ((uri.toString()).contains(properties.getProperty("LINK_RESOURCE")))
						public_network = uri.toString();
				}
			}

			if (public_network != null && !public_network.isEmpty()) {
				System.out.println("\nPublic Network = " + public_network);
				// Unlink network interface that doesn't have public IP
				//System.out.println(networkInterfaceLocation);
	                        client.delete(URI.create(networkInterfaceLocation));

				NetworkInterface ni = eb.getNetworkInterface();
                	        ni.setSource(vm_resource);
                        	ni.setTarget(public_network);
	                        location = client.create(ni);

        	                //Thread.sleep(5000);
        		} else System.out.println("No available network found!");

			// Getting the public interface occi.core.id
			String tmp = "";
			String public_networkInterfaceLocation = "";
			String public_networkInterfaceLocation_stripped = "";

			entities = client.describe(URI.create(properties.getProperty("OCCI_RESOURCE_ID")));
			for (Entity entity : entities) {
				Resource resource = (Resource) entity;
                                Set<Link> links = resource.getLinks(NetworkInterface.TERM_DEFAULT);
                                for (Link link : links) {
                                	tmp = link.getValue(IPNetworkInterface.ADDRESS_ATTRIBUTE_NAME);
                                        if (checkIP(tmp) != null) {
                                        	public_networkInterfaceLocation = link.getKind().getLocation()
                                                                                + link.getId();

						public_networkInterfaceLocation_stripped =
                                                public_networkInterfaceLocation.replace("/network/interface/","");

	                                        public_networkInterfaceLocation = "/network/interface/" 
							+ public_networkInterfaceLocation_stripped;
                                        }
                                }
                        }

                        System.out.println(properties.getProperty("OCCI_ENDPOINT_HOST") 
					+ public_networkInterfaceLocation);

		} // end 'link' => network

		else {
                        System.out.println("[+] Attach a volume to the VM");
                        System.out.println(properties.getProperty("OCCI_RESOURCE_ID"));
                        System.out.println("[-] VolumeID");
                        System.out.println(properties.getProperty("LINK_RESOURCE"));

                        StorageLink sl = eb.getStorageLink();
			sl.setSource(properties.getProperty("OCCI_RESOURCE_ID"));
			sl.setTarget(properties.getProperty("LINK_RESOURCE"));
			URI storageLinkLocation = client.create(sl);
        	        Thread.sleep(5000);
		} // end 'link' => volume


	} catch (CommunicationException | URISyntaxException |
		 EntityBuildingException | InvalidAttributeValueException | InterruptedException ex) {
                throw new RuntimeException(ex);
        }
    }

    public static void main (String[] args)
    {
	// [ Setting default preferences ]
	String AUTH = "x509"; 
        String OCCI_ENDPOINT_HOST = "https://carach5.ics.muni.cz:11443"; // <= Change here!
	//String OCCI_ENDPOINT_HOST = "https://rocci.iihe.ac.be:11443"; // <= Change here!
        //String OCCI_ENDPOINT_HOST = "https://fedcloud-cmdone.egi.cesga.es:11443"; // <= Change here!
        //String OCCI_ENDPOINT_HOST = "http://stack-server.ct.infn.it:8787/occi1.1"; // <= Change here!
        String TRUSTED_CERT_REPOSITORY_PATH = "/etc/grid-security/certificates";
        String PROXY_PATH = "/tmp/x509up_u1041"; // <= Change here!
	Boolean verbose = true;

	// [ *Attach* a volume to a VM ]
	String ACTION = "link"; 

	// CESNET-MetaCloud
	List<String> RESOURCE = Arrays.asList("compute", 
	"https://carach5.ics.muni.cz:11443/compute/102559"); // <= Change here!
	String LINK_RESOURCE = ("https://carach5.ics.muni.cz:11443/storage/7194"); // <= Change here!
	
	/*List<String> RESOURCE = Arrays.asList("storage", 
	"https://carach5.ics.muni.cz:11443/compute/73206"); // <= Change here! (??)
	String LINK_RESOURCE = ("/link/storagelink/compute_74020_disk_2"); // <= Change here!*/

	// BIFI
        //List<String> RESOURCE = Arrays.asList("compute", 
        //"http://server4-eupt.unizar.es:8787/compute/67779542-2fb0-46d6-901f-ffea8028254d"); // <= Change here!
        //String LINK_RESOURCE = ("/network/public"); // <= Change here!

	//List<String> RESOURCE = Arrays.asList("storage", 
        //"http://server4-eupt.unizar.es:8787/compute/67779542-2fb0-46d6-901f-ffea8028254d"); // <= Change here!
        //String LINK_RESOURCE = ("http://server4-eupt.unizar.es:8787/storage/e5102e95-5cde-4db5-bf85-2677c5359203");


	if (verbose) {
		System.out.println();
		if (ACTION != null && !ACTION.isEmpty()) 
			System.out.println("[ACTION] = " + ACTION);
		else	
			System.out.println("[ACTION] = Get dump model");
		System.out.println("AUTH = " + AUTH);
		//System.out.println("VOMS = " + VOMS);
		if (OCCI_ENDPOINT_HOST != null && !OCCI_ENDPOINT_HOST.isEmpty()) 
			System.out.println("OCCI_ENDPOINT_HOST = " + OCCI_ENDPOINT_HOST);
		if (RESOURCE != null && !RESOURCE.isEmpty()) 
			System.out.println("RESOURCE = " + RESOURCE);
		if (TRUSTED_CERT_REPOSITORY_PATH != null && !TRUSTED_CERT_REPOSITORY_PATH.isEmpty()) 
			System.out.println("TRUSTED_CERT_REPOSITORY_PATH = " + TRUSTED_CERT_REPOSITORY_PATH);
		if (PROXY_PATH != null && !PROXY_PATH.isEmpty()) 
			System.out.println("PROXY_PATH = " + PROXY_PATH);
                if (LINK_RESOURCE != null && !LINK_RESOURCE.isEmpty())
                        System.out.println("LINK_RESOURCE = " + LINK_RESOURCE);
		if (verbose) System.out.println("Verbose = True ");
		else System.out.println("Verbose = False ");
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
        		properties.setProperty("OCCI_RESOURCE_ID", RESOURCE.get(i));
        		
		else { 
			properties.setProperty("RESOURCE", RESOURCE.get(i));
        		properties.setProperty("OCCI_RESOURCE_ID", "empty");
		}
        }
			
        properties.setProperty("LINK_RESOURCE", LINK_RESOURCE);
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

		if  (ACTION.equals("link")) 
			doLink(properties, eb, client, model);

	} catch (CommunicationException ex ) {
                 throw new RuntimeException(ex);
        }
    }
}
