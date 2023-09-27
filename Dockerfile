# Verwende das offizielle OpenJDK-Image als Basis
FROM openjdk:17

# Setze das Arbeitsverzeichnis im Container
WORKDIR /app

# Kopiere die JAR-Datei deiner Spring Boot-Anwendung in den Container
COPY target/Hackers-Security1-0.0.1-SNAPSHOT.jar hackers-security-app.jar

# Exponiere den Port, den deine Spring Boot-Anwendung verwendet (standardmäßig 8080)
EXPOSE 8080

# Definiere den Startbefehl für deine Anwendung
CMD ["java", "-jar", "hackers-security-app.jar"]