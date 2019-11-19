FROM adoptopenjdk/openjdk11-openj9:jdk-11.0.1.13-alpine-slim

COPY target/vuetinaut-*.jar vuetinaut.jar
COPY ui/dist /opt/ui/dist

EXPOSE 8080

CMD java -Dcom.sun.management.jmxremote -noverify ${JAVA_OPTS} -jar vuetinaut.jar