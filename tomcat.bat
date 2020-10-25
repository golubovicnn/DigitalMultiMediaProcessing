echo off

SET CATALINA_BASE=tools\tomcat\apache-tomcat-9.0.0.M1
SET CATALINA_HOME=tools\tomcat\apache-tomcat-9.0.0.M1
SET CATALINA_OPTS=-Xmx512m

if ""%1"" == ""start"" goto start_tomcat
if ""%1"" == ""stop"" goto stop_tomcat

    echo usage: "tomcat start" or "tomcat stop"
    echo NOTE: make sure your JAVA_HOME is set properly and no process is running on port 8080!
    goto end

:start_tomcat

    echo Starting tomcat...
    echo The application should be available at http://localhost:8080/itm/
    %CATALINA_BASE%\bin\startup.bat
    goto end

:stop_tomcat
    echo "starting tomcat..."
    %CATALINA_BASE%\bin\shutdown.bat
    goto end

:end