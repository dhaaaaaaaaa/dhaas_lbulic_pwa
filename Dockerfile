# --- Stage 1: Build ---
# --- Stage 1: Build ---
FROM eclipse-temurin:22-jdk AS build
WORKDIR /app

# Kopiere den gesamten Projektinhalt in den Container
COPY . .

# Installiere Gradle manuell (da der Wrapper fehlt und apt evtl. altes Gradle hat)
RUN apt-get update && apt-get install -y unzip wget && \
    wget https://services.gradle.org/distributions/gradle-8.10-bin.zip -P /tmp && \
    unzip -d /opt/gradle /tmp/gradle-8.10-bin.zip && \
    ln -s /opt/gradle/gradle-8.10/bin/gradle /usr/bin/gradle

# Baue die JAR-Datei
RUN gradle bootJar --no-daemon -x test

# --- Stage 2: Run ---
# Wir nutzen hier ein schlankes JRE Image für die Ausführung
# --- Stage 2: Run ---
FROM eclipse-temurin:22-jre
WORKDIR /app

# Kopiere das erstellte JAR aus dem Build-Stage
# Gradle legt Artefakte standardmäßig unter build/libs/ ab
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]