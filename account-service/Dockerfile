# Build stage
FROM maven:3.8.3-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY account-service/pom.xml ./account-service/
COPY account-service/src ./account-service/src
RUN mvn -f account-service/pom.xml clean package -DskipTests

# Run stage
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/account-service/target/account-service-*.jar ./app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/app/app.jar"]