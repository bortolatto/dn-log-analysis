# Overview

This tool aims to analyze logs with an specific format and to extract several pieces of information from it, such as how many events have occurred and the timestamps that it happened.
For testing purposes, please download file from http://debreuck.neirynck.com/opdracht/server.zip.

# Pre requisites
*  JDK 11 or later
*  Maven 3 or later

# Setup the application
Just clone the source code from this repository and type the following Maven command:
```
mvn clean verify
```
After that, you should see an artifact in target folder named log-analysis-1.0-SNAPSHOT.jar.

# Testing the application
Just run the application with the file log argument: 
```
java -jar log-analysis-1.0-SNAPSHOT.jar <filename> (without <> characters)
```

The progress of the analysis will be displayed on the console:
```
20:11:26.155 [main] INFO  Application - Reading file /Users/bortolatto/Downloads/server.log
20:11:26.381 [main] INFO  Application - Scanning file...
20:11:27.293 [main] INFO  Application - Analysing startRendering events...
20:11:27.987 [main] INFO  Application - Analysing getRendering events...
20:11:28.546 [main] INFO  Application - Merging documents and events...
20:11:28.565 [main] INFO  Application - Generating summary...
20:11:28.569 [main] INFO  Application - Persisting content into XML file named server.log.xml
20:11:29.352 [main] INFO  Application - Success!
```