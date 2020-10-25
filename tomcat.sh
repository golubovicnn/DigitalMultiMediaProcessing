#!/bin/sh

CATALINA_BASE=tools/tomcat/apache-tomcat-9.0.0.M1
CATALINA_OPTS=-Xmx512m
export CATALINA_OPTS

if [ "$1" = "start" ]; then

  echo "starting tomcat..."
  $CATALINA_BASE/bin/startup.sh

elif [ "$1" = "stop" ]; then

  echo "stopping tomcat..."
  $CATALINA_BASE/bin/shutdown.sh

else

  echo "usage: tomcat.sh [start|stop]"
  exit 1

fi
