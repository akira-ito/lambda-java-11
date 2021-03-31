FROM openjdk:11
LABEL maintainer="edson.akira.ito"

ARG JAR_FILE=target/*.jar

WORKDIR /app
COPY ${JAR_FILE} api.jar

EXPOSE 8080

CMD ["java", "-jar", "api.jar"]