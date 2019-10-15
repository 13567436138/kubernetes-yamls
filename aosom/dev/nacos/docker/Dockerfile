FROM openjdk:8-jdk-alpine

MAINTAINER HXP

COPY nacos-server.jar /
ADD cluster.conf /root/nacos/conf/

EXPOSE 8848

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /nacos-server.jar" ]