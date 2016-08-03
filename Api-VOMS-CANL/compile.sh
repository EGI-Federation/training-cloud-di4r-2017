#/bin/sh
#
#  @author Giuseppe LA ROCCA - EGI.eu
#  @mail giuseppe.larocca@egi.eu
#  @date 29/06/2016
#
# Export the needed environment variables, 
# compile and run the class.

unset CLASSPATH

export CLASSPATH=.:\
./jars/cog-jglobus.jar:\
./jars/log4j-1.2.14.jar:\
./jars/bcprov-jdk16-1.46.jar:\
./jars/bcmail-jdk16-1.46.jar:\
./jars/bcpkix-jdk15on-1.50.jar:\
./jars/voms-api-java-3.0.3.jar:\
./jars/commons-io-2.4.jar:\
./jars/commons-cli-1.2.jar:\
./jars/voms-clients-3.0.4.jar:\
./jars/canl-1.3.1.jar

rm -rf *.class 2>/dev/null

${JAVA_HOME}/bin/javac -classpath ${CLASSPATH} VOMSProxyInit.java
[ $? -eq 1 ] && exit 1

${JAVA_HOME}/bin/java -classpath $CLASSPATH VOMSProxyInit 
[ $? -eq 1 ] && exit 1
