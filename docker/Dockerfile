FROM centos

RUN yum -y update \ 
	&& yum -y upgrade
RUN yum install -y java-1.8.0-openjdk

ADD oauth-service.tar /
COPY run.sh /
RUN chmod +x run.sh

CMD /run.sh