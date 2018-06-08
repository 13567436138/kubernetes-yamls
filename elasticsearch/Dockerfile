FROM centerforopenscience/elasticsearch:2.4
ADD  plugins/ /usr/share/elasticsearch/plugins/
ADD  config/synonym.txt /usr/share/elasticsearch/config/analysis/
USER root
RUN mkdir /home/elasticsearch && chown elasticsearch:elasticsearch /home/elasticsearch
ADD  java.policy /etc/java-8-openjdk/security/
USER elasticsearch
ADD home/elasticsearch /home/elasticsearch/
ADD  elasticsearch.yml config/elasticsearch.yml