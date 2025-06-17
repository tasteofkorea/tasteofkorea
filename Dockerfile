FROM openjdk:21-jdk-slim
WORKDIR /app

ARG JAR_FILE=build/libs/*.jar
COPY --chown=root:root ${JAR_FILE} /app/app.jar


ENTRYPOINT ["java", "-jar", "./app.jar"]
