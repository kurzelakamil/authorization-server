# Start with a base image containing Java runtime
FROM openjdk:11-jdk-slim

# Add Maintainer Info
LABEL maintainer="kurzela.kamil@gmail.com"

# Make port 7070 available to the world outside this container
EXPOSE 7070

# The application's jar file
ARG JAR_FILE=authorization-server.jar

# Add the application's jar to the container
ADD ${JAR_FILE} authorization-server.jar

ADD run.sh run.sh

# Run the jar file
RUN apt-get update && apt-get install -y netcat
RUN chmod +x run.sh
CMD /bin/sh run.sh