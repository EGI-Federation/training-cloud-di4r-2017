#!/usr/bin/env python

#
#  Copyright 2016 EGI Foundation
# 
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.
#
#/

import httplib
import xmltodict

__author__    = "Giuseppe LA ROCCA"
__email__     = "giuseppe.larocca@egi.eu"
__version__   = "$Revision: 0.0.7"
__date__      = "$Date: 23/08/2016 08:50:22"
__copyright__ = "Copyright (c) 2016 EGI Foundation"
__license__   = "Apache Licence v2.0"

vo = "training.egi.eu"				# <== Change here!

def appdb_call(c):
        conn  =  httplib.HTTPSConnection('appdb.egi.eu')
        conn.request("GET", c)
        data = conn.getresponse().read()
        conn.close()
        data.replace('\n','')
        return xmltodict.parse(data)

def get_provider_metadata(id):

        try:
                # E.g.: https://appdb.egi.eu/rest/1.0/va_providers/8253G0
                data = appdb_call('/rest/1.0/va_providers/%s' %id)

                provider_name = data['appdb:appdb']['virtualization:provider']['provider:name']

                if (data['appdb:appdb']['virtualization:provider'].has_key('provider:endpoint_url')):
                        provider_endpoint_url = data['appdb:appdb']['virtualization:provider']['provider:endpoint_url']
                else:
                        provider_endpoint_url = 'N/A'

                site_name = data['appdb:appdb']['virtualization:provider']['appdb:site']['site:officialname']
                status = data['appdb:appdb']['virtualization:provider']['appdb:site']
                url = ("https://appdb.egi.eu/rest/1.0/va_providers/%s" %id)

        except:
                print ""

        print "\n\n %s [%s] " %(provider_name, id)
        print " \t--> Sitename: %s \
                \n\t--> Endpoint: %s \
                \n\t--> Status: %s \
                \n\t--> URL: %s" \
                % (site_name,
                   provider_endpoint_url,
                   status['@status'],
                   url)


def get_resource(id):

        try:
                # E.g.: https://appdb.egi.eu/rest/1.0/va_providers/8253G0
                data = appdb_call('/rest/1.0/va_providers/%s' %id)

                for resource_tpl in data['appdb:appdb']['virtualization:provider']['provider:template']:
                        print "\t%s" %resource_tpl['provider_template:resource_id']

        except:
                print ""


def get_va(id):

        try:
                # E.g.: https://appdb.egi.eu/rest/1.0/va_providers/8253G0
                data = appdb_call('/rest/1.0/va_providers/%s' %id)

                for os_tpl in data['appdb:appdb']['virtualization:provider']['provider:image']:
                        try:
                                if vo in os_tpl['@voname']:
                                        print "\t - Name = %s [v%s] " %(os_tpl['@appname'], os_tpl['@vmiversion'])
                                        print "\t - OCCI ID = %s" %os_tpl['@va_provider_image_id']
                                        print "\t - URI = %s" %os_tpl['@mp_uri']
                        except:
                                print ""

        except:
                print ""



def main():

        print "\n ~ Listing providers that have subscribed the [%s] VO" %vo

        try:
                # E.g. https://appdb.egi.eu/rest/1.0/sites?flt=%%2B%%3Dvo.name:training.egi.eu&%%2B%%3Dsite.supports:1
                data = appdb_call('/rest/1.0/sites?flt=%%2B%%3Dvo.name:%s&%%2B%%3Dsite.supports:1' %vo)

                providersID = []
                for site in data['appdb:appdb']['appdb:site']:
                        if  type(site['site:service']) == type([]):
                                for service in site['site:service']:
                                        #print "%s" %service['@id']
                                        providersID.append(service['@id'])
                        else:
                                providersID.append(site['site:service']['@id'])

                # Get provider metadata
                for ID in providersID:
                        get_provider_metadata(ID)
                        print "\n ~ Listing available resource(s) templates"
                        get_resource(ID)
                        print "\n ~ Listing available Virtual Appliance(s)"
                        get_va(ID)
                        print ""

        except:
                print ""

if __name__ == "__main__":
        main()
