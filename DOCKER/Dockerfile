# Use OpenJDK 17 Alpine base image
FROM openjdk:17-alpine

# Set the working directory in the Docker image
WORKDIR /app

# Copy necessary files for the build
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src
COPY version.properties .

# Grant execution rights on the gradlew script
RUN chmod +x ./gradlew

# Build the application using Gradle
RUN ./gradlew build -x test

# Argument to pass the JAR file name
ARG JAR_FILE

# Copy the built JAR file into the image
COPY build/libs/$JAR_FILE app.jar

# Expose port 8080 for the application
EXPOSE 8080

# Command to run the Spring Boot application
CMD ["java", "-jar", "app.jar"]
