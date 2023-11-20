FROM maven:3.8.4-openjdk-8-slim AS Builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
COPY wait-for-services.sh .
RUN chmod +x wait-for-services.sh
RUN mvn clean package
FROM openjdk:8-jre-alpine
RUN apk add --no-cache wget netcat-openbsd bash
WORKDIR /app
COPY --from=Builder /app/target/demo-0.0.1-SNAPSHOT.jar app.jar
COPY --from=Builder /app/wait-for-services.sh .
EXPOSE 8080
CMD ["bash", "-c", "./wait-for-services.sh && java -Dspring.profiles.active=prod -jar app.jar"]
