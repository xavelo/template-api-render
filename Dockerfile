#
# Build stage
#
FROM maven:3.9.6-eclipse-temurin-21 AS build
COPY . .
RUN mvn clean package -DskipTests

#
# Package stage
#
FROM eclipse-temurin:21-jre
COPY --from=build /app/target/*.jar myapp.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","myapp.jar"]

