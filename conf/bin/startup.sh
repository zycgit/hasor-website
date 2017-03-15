#!/bin/sh
# ----------------------------------------------------------------------------

mkdir -p "$WEBSITE_HOME/logs/nginx"
mkdir -p "$WEBSITE_HOME/logs/tomcat"
mkdir -p "$WEBSITE_HOME/logs/rsf/"

/usr/local/nginx/sbin/nginx

cd $CATALINA_HOME
exec "catalina.sh" "$@"