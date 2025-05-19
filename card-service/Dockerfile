FROM maven:3.8.3-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY card-service/pom.xml ./card-service/
COPY card-service/src ./card-service/src
RUN mvn -f card-service/pom.xml clean package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/card-service/target/card-service-*.jar ./app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]