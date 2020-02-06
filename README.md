# Overview

This tool aims to analyze logs with a specific format and to extract several pieces of information from it, such as how many events have occurred and the timestamps that it happened.
For testing purposes, please download the file from http://debreuck.neirynck.com/opdracht/server.zip.

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
14:41:43.931 [main] INFO  Application - Reading file /Users/bortolatto/Downloads/server.log
14:41:44.134 [main] INFO  Application - Scanning file...
14:42:23.503 [main] INFO  Application - Analysing startRendering events...
14:42:24.571 [main] INFO  Application - Analysing getRendering events...
14:42:26.251 [main] INFO  Application - Merging documents and events...
14:42:26.274 [main] INFO  Application - Generating summary...
14:42:26.276 [main] INFO  Application - Persisting content into XML file named server.log.xml
14:42:27.001 [main] INFO  Application - Success!
```