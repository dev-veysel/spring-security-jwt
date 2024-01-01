FROM openjdk:17
LABEL maintainer="veysel@gmx.de"
WORKDIR /app
COPY target/*.jar /app/hackers-security-app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "hackers-security-app.jar"]