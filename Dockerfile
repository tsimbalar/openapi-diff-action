FROM openjdk:13-jdk-alpine

WORKDIR /openapi-diff-action

COPY target/openapi-diff-action.jar openapi-diff-action.jar

ENTRYPOINT ["java", "-jar", "openapi-diff-action.jar"]