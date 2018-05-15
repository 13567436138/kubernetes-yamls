FROM jetty:9.2.24-jre7
ADD dubbo-admin-2.0.0.war /var/lib/jetty/webapps/root.war
USER root
RUN mkdir -p /home/jetty && chown jetty:jetty /home/jetty
USER jetty
EXPOSE 8080
ENTRYPOINT  ["java","-Djava.security.egd=file:/dev/./urandom", "-jar" ,"/usr/local/jetty/start.jar"]

