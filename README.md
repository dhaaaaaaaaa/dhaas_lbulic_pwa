# GEK916 Informationssysteme "PWA Deployment"

## Arbeitsschritte

### CI/CD
Pfad .github/workflows/ dort Datei "ci.yml" erstellen:
````yml
name: CI

on:
  push:
    branches: [ "main" ]
  pull_request:
    branches: [ "main" ]

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout repository
        uses: actions/checkout@v4

      - name: Set up JDK 22
        uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: "22"

      - name: Set up Gradle
        uses: gradle/actions/setup-gradle@v5
        with:
          cache-encryption-key: ${{ secrets.GRADLE_ENCRYPTION_KEY }}

      - name: Backend - build & test
        run: ./gradlew clean test

      - name: Upload backend test reports
        if: always()
        uses: actions/upload-artifact@v4
        with:
          name: backend-test-report
          path: |
            build/test-results/test
            build/reports/tests/test

      - name: Set up Node.js
        uses: actions/setup-node@v4
        with:
          node-version: "20"
          cache: "npm"
          cache-dependency-path: frontend/package-lock.json

      - name: Frontend - install dependencies
        working-directory: frontend
        run: npm ci

      - name: Frontend - build
        working-directory: frontend
        run: npm run build

````
Zuerst schlug unser Workflow beim Schritt ./gradlew clean test fehl:
"Permission denied", wir mussten gradlew adden:
````
chmod +x gradlew
````

Später beim Laufen der Tests im CI schlugen alle Tests fehl, weil die Anwendung eine Postgres-Datenbank im Schema VENLAB erwartete. Wir haben dann Test dafür gesetzt damit ging es


## Probleme die wir hatten

### Problem .gitignore
Wir haben gitignore nicht richtig gesetzt im Projektpfad, und in unserem alten Repo haben wir probiert Commits zu löschen und es neu raufzuladen aber es ging nicht weil alle Dateien schon von früher gespeichert waren

### Problem CI
Wir haben als wir die CI pipeline raufgepusht haben rotes kreuz gehabt weil der Runner die Datei gradlew nicht ausführen darf
Lösung war:
````
chmod +x gradlew
git add gradlew
git commit -m ""
git push
````

### Problem CI 2.0
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

### Test bei CI fehlgeschlagen
````
./gradlew clean test
````
Das erzeugt ein testprotokoll

Ergebnis war es failed bei den Tests weil wir ja unsere env datei im gitignore hatten

#### Lösung: Testumgebung erstellen
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

## Cloud 

Wir wollen das Projekt jetzt in die Cloud erweitern. Am einfachsten geht das mit einem Droplet von DigitalOcean. Da müssen wir uns zuerst das GitHub Student Angebot holen und eine Zahlungsmethode ähnlich wie bei FireBase Studio hinzufügen. Nachdem wir das gemacht haben stehen uns 200$ zur Verfügung (was ca. für ein Jahr reicht). Somit können wir problemlos unser erstes Droplet erstellen (Ubuntu) mit allen Standardeinstellungen. Beim Erstellen des Droplets können wir uns entweder in unserer host cli damit rumquälen einen ssh key zu adden oder wir machen das schnell über die GUI von DigitalOcean. Nach dem erstellen des SSH Keys können wir 

Docker installieren in der Web Konsole <br>
 
zuerst apt update <br>

Danach root@ubuntu-s-2vcpu-4gb-fra1-01:~# apt install -y docker.io docker-compose-plugin
Reading package lists... Done
Building dependency tree... Done
Reading state information... Done
E: Unable to locate package docker-compose-plugin

aber das ist zu alt also 

root@ubuntu-s-2vcpu-4gb-fra1-01:~# apt install -y docker.io docker-compose


# Quellen
[1]: Digital Ocean Droplet Dokumentation https://docs.digitalocean.com/products/droplets/ 


