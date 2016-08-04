# Api-VOMS-CANL

Api-VOMS-CANL is a Java binding for the Virtual Organization Membership Service (VOMS) API. The application contact the VOMS service to generate RFC or full-legacy X.509 proxy certificates.

## Compile and Run

Edit the source code in VOMSProxyInit.java and specify your preferred settings:

```
[..]
String VONAME = "training.egi.eu"; // <= Change here!
String VOMS_PROXY_FILEPATH = "/tmp/x509up_u1000"; // <= Change here!
String VOMS_LIFETIME = "24:00";
```

Compile and run the source code:

```
./compile.sh
```

## Dependencies

Api-VOMS-CANL uses:
- bcmail-jdk16-1.46.jar
- bcpkix-jdk15on-1.50.jar
- bcprov-jdk16-1.46.jar
- canl-1.3.1.jar
- cog-jglobus.jar
- commons-cli-1.2.jar
- commons-io-2.4.jar
- log4j-1.2.14.jar
- voms-api-java-3.0.3.jar
- voms-clients-3.0.4.jar
