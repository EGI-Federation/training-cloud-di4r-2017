
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

import java.io.File;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import java.net.URI;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.net.URISyntaxException;

public class CheckIP
{

    public static boolean testIpAddress(byte[] testAddress)
    {
        Inet4Address inet4Address;
        boolean result=false;

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
                String _IP0 =
                        matcher.group(1).replace(".","");

                String _IP1 =
                        matcher.group(2).replace(".","");

                String _IP2 =
                        matcher.group(3).replace(".","");

                String _IP3 =
                        matcher.group(4).replace(".","");

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

    public static void main (String[] args)
    {
	String IP = args[0];

        if (checkIP(IP) != null)
		System.out.println("The VA has started with a *public* IP [ " + IP + " ]");
        else
        	System.out.println("The VA has started with a *private* IP [ " + IP + " ]");
    }
}
