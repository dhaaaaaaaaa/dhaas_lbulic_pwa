# Einführung

Bei dieser Übung soll die vorhandene Implementierung in der Cloud deployed werden und auch als PWA für mobile Endgeräte angeboten werden.

# Ziele 

Das Ziel dieser Übung ist das Bereitstellen einer Webanwendung in der Cloud. Dabei sollen Werkzeuge zum Einsatz kommen, die das leichte Umsetzen, Testen und Bereitstellen beschleunigen. Bestehender Code soll dabei angepasst und erweitert werden ohne die Funktionalität zu beeinträchtigen.

# Voraussetzungen

- Docker Compose
- Kenntnisse über Javascript
- Funktionstüchtige ReST Schnittstelle aus "ReST Backend"
- Funktionstüchtige JS-Applikation aus "Vue CRUD"
- Kenntnisse über CI/CD-Workflow oder Zusammenarbeit mit DevOps Engineer

# Grundanforderungen

- PWA Konfiguration
- Cloud-Deployment mit einfachem Login
- Einbindung von CI/CD-Workflow mit Testreports
- Auswahlfeld von zu anzeigenden Attributen der einzelnen Tabellen
- Theme mit dark/light Switch

# Umgesetzte Änderungen 

## Digital Ocean
Einer der einfachsten Wege zur Cloud ist durch ein Droplet von DigitalOcean. Da haben wir uns zuerst das GitHub Student Pack geholt mit dem wir ein Credit von 200$ haben (ein Jahr Ablaufzeit) und das ist mehr als genug für unser Projekt. Droplet wird erstellt mit Standardeinstellungen, läuft auf Ubuntu und ist über eine Public IP erreichbar. Davor musste ein SSH Key hinzugefügt werden (oder wenn man keinen hat schnell erstellen, kurzes Tutorial ist eh da direkt dabei) 



# Probleme bei der Umsetzung 



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
