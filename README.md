# Deployment 
For our deployment strategy, we are intending to use Docker (the included dockerfile only works for Linux machines) and Maven (works 100%).

The choice for Docker is to create containers that are platform independent. We want the application to run as is regardless of whatever machine the user is using.  

The choice for Maven is to ensure that the project compiles and that test cases completely pass before packaging it into a JAR file.

## Requirements
- Maven 
- Java 8+
- Docker
- Linux (preferred and tested)

## Maven 
In the same directory as `pom.xml`, run:
```
mvn clean install && cd target/ && \
java -jar EdgeConverter-1.0-SNAPSHOT-jar-with-dependencies.jar
```

## Docker
In the same directory as `Dockerfile`, run:
```
docker build -t edgeconvert .

docker run -e DISPLAY=$DISPLAY -v /tmp/.X11-unix:/tmp/.X11-unix edgeconvert
```

Cleanup (remove containers + images):
```
docker rm $(docker ps -a -q)

docker image rm $(docker images -a -q)
```