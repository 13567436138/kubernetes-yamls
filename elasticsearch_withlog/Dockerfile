FROM centerforopenscience/elasticsearch:2.4
ADD  plugins/ /usr/share/elasticsearch/plugins/
#ADD  config/synonym.txt /usr/share/elasticsearch/config/analysis/
ADD  config/synonym.txt /etc/elasticsearch/analysis/
USER root
RUN mkdir /home/elasticsearch && chown elasticsearch:elasticsearch /home/elasticsearch
ADD  java.policy /etc/java-8-openjdk/security/
USER elasticsearch
ADD home/elasticsearch /home/elasticsearch/
ADD  logging2.yml /usr/share/elasticsearch/config/logging.yml
ADD  logging2.yml /etc/elasticsearch/logging.yml
ADD  elasticsearch.yml config/elasticsearch.yml
ADD  elasticsearch.yml /etc/elasticsearch/elasticsearch.yml