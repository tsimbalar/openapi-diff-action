FROM openjdk:13-alpine

COPY target/openapi-diff-action.jar /openapi-diff-action/openapi-diff-action.jar

ENTRYPOINT ["java", "-jar", "/openapi-diff-action/openapi-diff-action.jar"]