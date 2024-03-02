FROM eclipse-temurin:17-jre

WORKDIR /backend
COPY build/libs/*.jar app.jar

EXPOSE 8085
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
