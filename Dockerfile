FROM openjdk:17-alpine
add target/admin-service.jar admin-service.jar
ENTRYPOINT [ "java", "-jar", "admin-service.jar" ]
