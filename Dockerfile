FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/minhductran-0.0.1-SNAPSHOT.jar intern.jar

EXPOSE 8081

CMD ["java", "-jar", "intern.jar"]