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

## PWA Konfiguration

*Vite-Plugin PWA in /frontend/vite.config.js:*

```
import { VitePWA } from 'vite-plugin-pwa'
...
VitePWA({
      registerType: 'autoUpdate',
      includeAssets: [...],
      manifest: {
        name: 'VenLab Dashboard',
        short_name: 'VenLab',
        description: 'Verwaltung von Laborproben und Analysen',
        theme_color: ...
```

autoUpdate bewirkt, dass der Benutzer nicht immer manuell refreshen muss. includeAssets werden im Cache gespeichert für Offline-Zugriff. manifest ist eine JSON die dem Browser sagt wie sich die App zu verhalen hat also heisst, aussieht usw. <br>
Das generierte WebApp Manifest befindet sich in ``` frontend/dist/manifest.webmanifest ```:

```
{
  "name": "VenLab Dashboard",
  "short_name": "VenLab",
  "description": "Verwaltung von Laborproben und Analysen",
  "start_url": "/",
  "display": "standalone",
  "background_color": "#ffffff",
  "theme_color": "#1976D2",
  "icons": [...]
}
```

*Service Worker:*

Der Service Worker ist ein js, das im Hintergrund läuft ``` frontend/dist/sw.js ```
```
self.skipWaiting(),
e.clientsClaim(),
e.precacheAndRoute([
  {url:"registerSW.js", revision:"..."},
  {url:"index.html", revision:"..."},
  {url:"assets/index-OX0AglLa.js", revision:null},
  ...
])
```
Und in ``` frontend/dist/registerSW.js  ```
```
if('serviceWorker' in navigator) {
  window.addEventListener('load', () => {
    navigator.serviceWorker.register('/sw.js', { scope: '/' })
  })
}
```
Wird gewartet bis die Seite geladen wird und dann der Service Worker gestartet

Er ist Teil der Web Worker API. Web Worker wurden eingeführt um js Multitasking zu ermöglichen. Der Service Worker ist eine spezielle Form davon, die dauerhaft im Browser registriert wird und sich an eine bestimmte IP bindet. Im Gegensatz zur Webseite selbst, die "stirbt" wenn man Tab schließt bleibt die Registrierung des Workers im Browser erhalten. Er wird vom Browser bei Bedarf aufgeweckt (wenn eine Anfrage reinkommt) und danach wieder schlafen gelegt.

#### Manifest: 
Das Manifest ist eine JSON-Datei, in der Name, Icons, Start-URL und Darstellungsart meiner PWA definiert sind. Der Browser nutzt diese Infos, um die App installierbar zu machen und sie wie eine native App darzustellen.

### Service Worker:
Der Service Worker läuft im Hintergrund, cached wichtige Ressourcen und ermöglicht damit Offline-Nutzung und schnellere Ladezeiten. Außerdem ist er ein zentraler Baustein, damit der Browser meine Seite als installierbare PWA erkennt und aktualisieren kann.

### https:
Eine PWA braucht HTTPS, weil der Service Worker nur in einer sicheren Umgebung laufen darf. Da er Netzwerkverkehr abfangen und verändern kann, erzwingen Browser eine verschlüsselte Verbindung, um Manipulationen zu verhindern und die Sicherheit der Nutzer zu gewährleisten.




## Cloud-Deployment mit einfachem Login

Einer der einfachsten Wege zur Cloud ist durch ein Droplet von DigitalOcean. Da haben wir uns zuerst das GitHub Student Pack geholt mit dem wir ein Credit von 200$ haben (ein Jahr Ablaufzeit) und das ist mehr als genug für unser Projekt. Droplet wird erstellt mit Standardeinstellungen, läuft auf Ubuntu und ist über eine Public IP erreichbar. Davor musste ein SSH Key hinzugefügt werden (oder wenn man keinen hat schnell erstellen, kurzes Tutorial ist eh da direkt dabei). Jetzt kann man die Verbindung zur Konsole per SSH starten und mit Terminalbefehlen arbeiten:

*Alles auf den neuesten Stand bringen:*
```
sudo apt update && sudo apt upgrade -y
```

*Docker Installation:*
```
sudo apt install docker-ce docker-ce-cli containerd.io docker-compose-plugin
```

*Unser Projekt rüberbringen:*
```
git clone https://github.com/dhaaaaaaaaa/dhaas_lbulic_pwa
```

*Die env Datei (weil im gitignore):*
```
nano .env
```
Und dann einfach den Inhalt händisch rüberkopieren vom lokalen Projekt <br>

*Dann das Backend gestartet:*
```
docker compose up -d
```
Dann kam kurz 502 Bad Gateway aber es dauert ca. eine halbe Minute bis man wirklich drauf zugreifen kann <br>
Nachdem dann kein direkter Error mehr geworfen wurde stand dann trocken da "No Data available" weil die .dat Dateien natürlich auch nicht mitgepullt wurden weil sie sich in der gitignore befanden. Rübekopieren per Host-Terminal hat dem Droplet nicht so sehr gefallen und er war sehr hartnäckig mit externen Verbindungen also haben wir die .dat Dateien mitgepusht und dann wieder gepullt mit dem Droplet 

```
docker compose up -d
```




## Einbindung von CI/CD-Workflow mit Testreports
Für CI müssen wir unter .github/workflow eine ci.yml Datei anlegen, Dieser Workflow wird getriggert bei jedem Push auf main oder jedem Pull Request auf main
````
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
build führt dabei wie folgt aus:
- zuerst Checkout des Repositories
- dann JDK 22 bereitstellen
- dann wird Gradle‑Wrapper verwendet 
- Am Ende wird das Backend mit Gradle gebaut und die Tests ausgeführt
- Ab Zeile 31: Nach dem Testlauf werden die von Gradle erzeugten Reports als Artefakt auch hochgeladen


Damit die Tests im CI ohne echte Postgres‑Datenbank laufen, haben wir ein eigenes Test‑Profil eingerichtet
Dafür haben wir Test application.properties eingerichtet und bei den Tests das Testprofil ausgewählt
Damit wird es beim Teststart eine frische In‑Memory‑DB angelegt und das Schema VENLAB automatisch erzeugt 



## Auswahlfeld von zu anzeigenden Attributen der einzelnen Tabellen
Um alle anzeigen zu lassen haben wir Standartmäßig ein Array erstellt welche alle Attribute anzeigt in AnalysisTable und SampleTable:
````

````
Oben im Header in der Klasse haben wir es dann anzeigen lassen:
````
<v-row class="px-4 pb-2">
  <v-col cols="12">
    <v-select
        v-model="selectedHeaders"
        :items="allHeaders"
        label="Spalten anzeigen/ausblenden"
        multiple
        return-object
        density="compact"
        variant="outlined"
        hide-details
        chips
        closable-chips
    ></v-select>
  </v-col>
</v-row>
````
v-select bindet dabei diese Dinge ein:
- :items="allHeaders" werden alle möglichen Spalten/Attribute ausgewählt
- v-model="selectedHeaders" → welche Spalten aktuell angezeigt werden
- multiple + chips ist f+r Mehrfachauswahl mit Chips, die man wegklicken kann
- Standartmäßig wird das model "selectedHeaders" ausgewählt also das was der User will







## Theme mit dark/light Switch





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
