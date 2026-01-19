# VenLab - Informationssysteme: Vue PWA & Cloud Deployment

## Einführung

In dieser Phase des Projekts wurde die bestehende VenLab-Applikation zu einer Progressive Web App (PWA) erweitert und für das Cloud-Deployment vorbereitet. Zudem wurde ein automatisierter CI/CD-Workflow implementiert, um die Qualität durch kontinuierliche Tests sicherzustellen.

## Ziele der Übung

- Konfiguration der Applikation als **Progressive Web App (PWA)** für mobile Geräte.
- Bereitstellung der Anwendung in der **Cloud**.
- Sicherung des Zugangs durch ein **Login-System**.
- Integration in einen **CI/CD-Workflow** mit automatisierten Testreports.
- Erweiterung der UI um ein **Dark/Light-Theme** und dynamische **Attribut-Auswahl**.

---

## Funktionen

### Progressive Web App (PWA)

- **Installierbar**: Die App kann als Icon auf dem Startbildschirm abgelegt werden.
- **Standalone-Modus**: Öffnet im Vollbildmodus ohne Browser-UI.
- **Offline-Fähigkeit**: Grundlegende Funktionen und schnelle Ladezeiten durch Service-Worker-Caching.
- **Manifest**: Konfiguriertes Web-Manifest mit Icons und Theme-Farben.

### Benutzeroberfläche

- **Design-System**: Implementierung mit Vuetify für ein konsistentes Erlebnis.
- **Theme-Switch**: Umschaltmöglichkeit zwischen Dark- und Light-Mode.
- **Dynamische Tabellen**: Benutzer können auswählen, welche Spalten (Attribute) in den Tabellen angezeigt werden sollen.

### Sicherheit & Backend

- **Login**: Zugriffsschutz durch ein API-Key-basiertes Login-Verfahren.
- **ReST-Schnittstelle**: Robustes Spring Boot Backend mit PostgreSQL-Anbindung.
- **Filterung & Paging**: Serverseitige Verarbeitung großer Datenmengen zur Performance-Optimierung.

---

## CI/CD Workflow

Der automatisierte Workflow ist in `.github/workflows/gradle.yml` definiert und umfasst folgende Schritte:

1. **Backend Unit-Tests**: Überprüfung der ReST-Schnittstelle und Datenbank-Integration.
2. **Frontend Build**: Kompilierung der Vue-Applikation.
3. **E2E-Tests**: Automatisierte End-to-End Tests mit Cypress zur Validierung der Benutzeroberfläche und Workflows.
4. **Artifacts**: Speicherung von Testreports (HTML), Screenshots und Backend-Logs für das Debugging.

---

## Voraussetzungen

- **Java 22** (Temurin Distribution)
- **Node.js 22**
- **Docker & Docker Compose**
- **PostgreSQL** (Port 5432)

---

### VenLab - Informationssysteme: Vue PWA & Cloud Deployment

## Anwendung

1. **Repository klonen:**

```
clone https://github.com/TGM-HIT/insy5-informationssysteme-vue-pwa-pwa_yeren_ashemsidini_eyueksel.git
```

2. **.env und .dat dateien ergänzen**

*.env (beispiel)*

```
POSTGRES_USER=[user]
POSTGRES_PASSWORD=[pw]
POSTGRES_DB=[db_name]

DB_USERNAME=[user]
DB_PASSWORD=[pw]
ADMIN_PASSWORD=[pw_für_login]

DB_URL=[db_url]
```

**.dat** dateien vom venlab_backup nehmen und im ordner in venlab_backup reinkopieren

3. **build**

```
docker compose up -d --build
```

---

## Bewertung & Status

- **PWA Konfiguration**: Abgeschlossen (Manifest, Icons, Offline-Support)
- **Cloud-Deployment**: Vorbereitet
- **CI/CD-Workflow**: Aktiv inklusive E2E-Tests und Reports
- **UI-Erweiterungen**: Dark Mode und Attribut-Filterung integriert
- **Login-Sicherung**: Implementiert (X-API-KEY Header)

**Bearbeitet:** Januar 2026
**Gruppengröße:** 3 Personen
# pwatest


# Lazo und ich

## Problem .gitignore
Wir haben gitignore nicht richtig gesetzt im Projektpfad, und in unserem alten Repo haben wir probiert Commits zu löschen und es neu raufzuladen aber es ging nicht weil alle Dateien schon von früher gespeichert waren

## Problem CI
Wir haben als wir die CI pipeline raufgepusht haben rotes kreuz gehabt weil der Runner die Datei gradlew nicht ausführen darf
Lösung war:
````
chmod +x gradlew
git add gradlew
git commit -m ""
git push
````

## Problem CI 2.0
Nachdem wir das oben gefixt hatten war das, das nächste Problem:
````
0s
Run ./gradlew clean test
Error: Unable to access jarfile /home/runner/work/dhaas_lbulic_pwa/dhaas_lbulic_pwa/gradle/wrapper/gradle-wrapper.jar
Error: Process completed with exit code 1.
````
Lokal klappt es aber auf GitHub ist die Datei nicht vorhanden, beim prüfen gesehen sie ist nicht da:

````
dominikhaas@MacBook-Air-von-Dominik jpa % ls gradle/wrapper
gradle-wrapper.properties
dominikhaas@MacBook-Air-von-Dominik jpa % 
````
Wird nicht gefunden weil wir sie im gitignore hatten, als Lösung erzeugen und auf GitHub pushen
````
brew install gradle
gradle wrapper
git add gradle/wrapper/gradle-wrapper.jar
git commit -m "Gradle wrapper jar für CI hinzugefügt"
git push
````
Danach ging CI

## Test bei CI fehlgeschlagen
````
./gradlew clean test
````
Das erzeugt ein testprotokoll

Ergebnis war es failed bei den Tests weil wir ja unsere env datei im gitignore hatten

### Lösung: Testumgebung erstellen
In build.gradle als dependencie hinzufügen:
    "runtimeOnly 'com.h2database:h2'"

Dann bei src/test/resources/ die Datei "application-test.properties" erstellen
Folgende Werte rein:
````java
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;INIT=CREATE SCHEMA IF NOT EXISTS VENLAB
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect
spring.jpa.properties.hibernate.default_schema=VENLAB

app.admin.password=test123
````

Dann RestbackendCiCdApplicationTests.java öffnen und Testprofil aktivieren:
````java
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RestbackendCiCdApplicationTests {
````

Dann im Terminal:
````
./gradlew clean test
BUILD SUCCESSFUL in 3s
````
