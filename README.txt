/*******************************************************************************

    R E A D M E
    
    This file is part of the WM.II.ITM course 2017
    (c) University of Vienna 2017
*******************************************************************************/


Installation/Deployment

    * download and extract the assignment zip
    * download and extract the lib zip
    * make sure you have a working JDK 1.5 installed
          o NOTE: since you will develop Java applications, you must install 
                  the Java Development Kit (JDK); the JRE has no compiler!
          o please follow the installation instructions for your OS (see wiki)
          o you can check your java version by calling "java -version" 
                and "javac -version" from the commandline
    * call "./ant.sh -p" (Linux/MacOSX/cygwin) or "ant.bat -p" (Windows shell) 
        to get a list of all possible build goals. 
    * call "./ant.sh" to build your sources.
Tomcat

    * call "./tomcat.sh" (Linux/MacOSX/cygwin) or "tomcat.bat" (Windows shell) 
        to get usage information about how to start/stop tomcat. 
        
    * Make sure no application is running under port 8080 on your machine (or 
        reconfigure tomcat accordingly)
    
    * call "./tomcat.sh start" respectively "tomcat start" to start tomcat and access
        the ITM application at http://localhost:8080/itm/
      
        
See the wiki for further information!
       

