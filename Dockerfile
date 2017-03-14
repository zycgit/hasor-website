FROM java:openjdk-8
MAINTAINER ZhaoYongChun "zyc@hasor.net"

# ------------------------------------- Install Software
# maven
ENV MAVEN_VERSION 3.3.9
RUN curl -fsSL http://project.hasor.net/hasor/develop/tools/apache/maven/$MAVEN_VERSION/apache-maven-$MAVEN_VERSION-bin.tar.gz | tar xzf - -C /usr/share \
        && mv /usr/share/apache-maven-$MAVEN_VERSION /usr/share/maven \
        && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn
ENV MAVEN_HOME /usr/share/maven
ENV M2_REPO /home/admin/maven-repository
RUN mkdir -p $M2_REPO && \
    sed -i '/<!-- localRepository/i\<localRepository>'$M2_REPO'</localRepository>' $MAVEN_HOME/conf/settings.xml

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

# Nginx.
RUN apt-get update && apt-get install -y nginx

# ------------------------------------- Config WORK_HOME
# work_home
ADD . /home/admin/hasorsite/source
ENV WEBSITE_HOME /home/admin/hasorsite
RUN mkdir -p "$WEBSITE_HOME/target" && \
    cp -R -f $WEBSITE_HOME/source/conf/env/online/env.config $WEBSITE_HOME/ && \
    cp -R -f $WEBSITE_HOME/source/conf/tomcat                $WEBSITE_HOME/tomcat && \
    cp -R -f $WEBSITE_HOME/source/conf/nginx                 $WEBSITE_HOME/nginx

# ------------------------------------- Setup Software
# tomcat
RUN rm -rf $CATALINA_HOME/conf    && ln -s $WEBSITE_HOME/tomcat   $CATALINA_HOME/conf && \
    rm -rf $CATALINA_HOME/logs    && ln -s $CATALINA_HOME/logs    $WEBSITE_HOME/logs && \
    rm -rf $CATALINA_HOME/deploys && ln -s $CATALINA_HOME/deploys $WEBSITE_HOME/target/deploys

# nginx
RUN mkdir -p "$WEBSITE_HOME/nginx/log" && \
    rm -rf /etc/nginx     && ln -s $WEBSITE_HOME/nginx /etc/nginx && \
    rm -rf /var/log/nginx && ln -s /var/log/nginx $WEBSITE_HOME/nginx/log && \
    rm -rf /var/www/html  && ln -s /var/www/html  $WEBSITE_HOME/nginx/www

# project
ENV WORK_HOME /home/admin/hasorsite
RUN cd /home/admin/hasorsite/source && \
    mvn clean package -Dmaven.test.skip=true && \
    mv `find . -name *.war` $WEBSITE_HOME/target/ROOT.war && \
    rm -rf $M2_REPO && \
    rm -rf $WEBSITE_HOME/source

# ------------------------------------- Run App
WORKDIR $CATALINA_HOME
EXPOSE 8080
EXPOSE 2160
EXPOSE 2161
EXPOSE 2162

#VOLUME /home/admin/hasorsite/logs
#VOLUME /home/admin/hasorsite/rsf
#VOLUME /home/admin/hasorsite/nginx

CMD ["catalina.sh", "run"]