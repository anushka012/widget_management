# Use a suitable base image that supports running Java applications
FROM adoptopenjdk:11-jre-hotspot

# Set the working directory
WORKDIR /app

# Copy the application code into the container
COPY . /app

# Expose the necessary port for the application (replace PORT_NUMBER with your actual port number)
EXPOSE 8080

# Specify the command to run the application within the container (replace JAR_FILE_NAME with your actual jar file name)
CMD ["java", "-jar", "widget_management-0.0.1-SNAPSHOT.jar"]
