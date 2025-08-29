# Utilise l'image officielle Java 21
FROM eclipse-temurin:21-jdk-alpine

# Crée un dossier pour l'application
WORKDIR /app

# Copie le fichier jar généré par Maven
COPY target/order-service-0.0.1-SNAPSHOT.jar app.jar

# Expose le port de l'application
EXPOSE 8081

# Commande de lancement
ENTRYPOINT ["java", "-jar", "app.jar"]
