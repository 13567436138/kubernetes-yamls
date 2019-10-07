FROM openjdk:8-jdk-alpine
MAINTAINER oasom

ENV JAVA_OPTS=""

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /stock-1.0.0-SNAPSHOT.jar" ]