FROM openjdk:7-jdk
MAINTAINER ZhaoYongChun "zyc@hasor.net"

# maven
ENV MAVEN_VERSION 3.3.9
RUN curl -fsSL http://project.hasor.net/hasor/develop/tools/apache/maven/$MAVEN_VERSION/apache-maven-$MAVEN_VERSION-bin.tar.gz | tar xzf - -C /usr/share \
        && mv /usr/share/apache-maven-$MAVEN_VERSION /usr/share/maven \
        && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn
ENV MAVEN_HOME /usr/share/maven
RUN mkdir -p "/home/admin/maven-repository" && \
    sed -i '/<!-- localRepository/i\<localRepository>/home/admin/maven-repository</localRepository>' $MAVEN_HOME/conf/settings.xml

# tomcat
ENV CATALINA_HOME /usr/local/tomcat
ENV PATH $CATALINA_HOME/bin:$PATH
RUN mkdir -p "$CATALINA_HOME"
WORKDIR $CATALINA_HOME
ENV TOMCAT_MAJOR 8
ENV TOMCAT_VERSION 8.5.0
ENV TOMCAT_TGZ_URL http://project.hasor.net/hasor/develop/tools/apache/tomcat/$TOMCAT_VERSION/apache-tomcat-$TOMCAT_VERSION.tar.gz
RUN set -x && \
	curl -fSL "$TOMCAT_TGZ_URL" -o tomcat.tar.gz && \
	tar -xvf tomcat.tar.gz --strip-components=1 && \
	rm bin/*.bat && \
	rm tomcat.tar.gz*
#
# work
ADD . /home/admin/hasorsite/source
ENV WEBSITE_HOME /home/admin/hasorsite
ENV WORK_HOME /home/admin/hasorsite
RUN mkdir -p "$WEBSITE_HOME/target" && \
    cp $WEBSITE_HOME/source/conf/work_home/online/env.config $WEBSITE_HOME/ && \
    ln -s $WEBSITE_HOME/source/conf/tomcat $WEBSITE_HOME/tomcat && \
    rm -rf $CATALINA_HOME/conf    && ln -s $WEBSITE_HOME/tomcat   $CATALINA_HOME/conf && \
    rm -rf $CATALINA_HOME/logs    && ln -s $CATALINA_HOME/logs    $WEBSITE_HOME/logs && \
    rm -rf $CATALINA_HOME/deploys && ln -s $CATALINA_HOME/deploys $WEBSITE_HOME/target/deploys

EXPOSE 8080
EXPOSE 8210

# === project ===
WORKDIR /home/admin/hasorsite/source
RUN mvn clean package -Dmaven.test.skip=true && \
    mv `find . -name *.war` $WEBSITE_HOME/target/ROOT.war

WORKDIR $CATALINA_HOME
CMD ["catalina.sh", "run"]