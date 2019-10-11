FROM jenkins/jnlp-slave:3.27-1

MAINTAINER HXP

USER root
#ADD apache-maven-3.6.2-bin.tar.gz /opt/
#RUN echo -e "\n">>~/.bash_profile && echo -e "export M2_HOME=/opt/apache-maven-3.6.2\n" >> ~/.bash_profile \
#	&& echo "export PATH=$PATH:$M2_HOME/bin">>~/.bash_profile && . ~/.bash_profile &&  ln -s  /opt/apache-maven-3.6.2/bin/mvn /usr/bin/mvn
RUN apt-get update && apt-get -y install maven 
ADD config.json /root/.docker/
ADD config  /root/.kube/
ADD kubectl /usr/bin/
RUN chmod  777 /usr/bin/kubectl
	
USER root	

ENTRYPOINT ["jenkins-slave"]