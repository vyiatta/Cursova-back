# Stage 1 Build
FROM openjdk:17-jdk-alpine AS build

# Change work directory inside container
WORKDIR /app

# Copy repository to the container
COPY . .

# Install Maven
RUN apk update && apk add --no-cache maven

# Build the application using Maven
RUN mvn package -Dmaven.test.skip

# Stage 2 Package
FROM openjdk:17-jdk-alpine

# Copy the built JAR file from the "build" container to a new container
COPY --from=build /app/target/project-0.0.1-SNAPSHOT.jar /app/cleanora.jar

# Define entry point for running the container
ENTRYPOINT ["java","-jar","/app/cleanora.jar"]
