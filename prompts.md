# KI-Prompts – Dokumentation der Nutzung

Im Rahmen der Übung wurde ein generatives KI-Tool (ChatGPT) zur
Analyse, Fehlersuche und Dokumentation verwendet.

---

## Prompt 1 – Analyse von CI/CD-Fehlern

**Ziel:**  
Analyse eines fehlschlagenden GitHub-Actions-Workflows im Zusammenhang
mit Cypress-End-to-End-Tests.

**Prompt (sinngemäß):**  
> Analysiere das gesamte Projekt und erkläre, warum die Cypress-Tests
> im CI/CD-Workflow nicht durchlaufen, ohne den Quellcode zu verändern.

**Ergebnis:**  
- Identifikation von Unterschieden zwischen lokaler Umgebung und CI
- Erkennung fehlender Datenbankinitialisierung und fehlender Secrets

---

## Prompt 2 – Datenbank & Docker Compose im CI

**Ziel:**  
Klärung, warum Docker Compose im CI fehlschlägt, obwohl es lokal funktioniert.

**Prompt (sinngemäß):**  
> Analysiere die Docker-Compose- und GitHub-Actions-Konfiguration und
> erkläre, warum die Datenbank im CI nicht korrekt startet.

**Ergebnis:**  
- Feststellung, dass `.env`-Dateien im CI nicht verfügbar sind
- Empfehlung zur Nutzung von GitHub Secrets zur Laufzeit

---

## Prompt 3 – Hibernate / JPA Fehleranalyse

**Ziel:**  
Analyse von Hibernate-Fehlern während des Gradle-Builds im CI.

**Prompt (sinngemäß):**  
> Analysiere die vollständigen CI-Logs und erkläre, warum der
> Gradle-Build mit Hibernate-Fehlern abbricht.

**Ergebnis:**  
- Identifikation von Konflikten zwischen `ddl-auto=update`
  und einer bereits initialisierten produktionsnahen Datenbank
- Erklärung, warum bestehende Views und Datentypen Schema-Updates verhindern

---

## Prompt 4 – Dokumentation & Changelog

**Ziel:**  
Erstellung einer professionellen Dokumentation für nicht behobene Probleme.

**Prompt (sinngemäß):**  
> Erstelle einen Changelog-Eintrag, der die vorgenommenen Änderungen,
> die Analyse und die Gründe beschreibt, warum das Problem nicht
> vollständig gelöst werden konnte.

**Ergebnis:**  
- Strukturierter, nachvollziehbarer Changelog
- Transparente Darstellung der technischen Limitierungen

---

## Hinweis
Die KI wurde ausschließlich zur Analyse, Strukturierung und
Formulierung technischer Inhalte verwendet.
Die Implementierung des Projekts erfolgte eigenständig.

# Prompt-Verzeichnis: Projekt-Dokumentation & Features

## 5. CI/CD & Testing (Changelog)
* **Inhalt:** Integration von Java 22, Gradle-Workflows und Cypress-Konfiguration.
* **Sinngemäßer Prompt:** *"Übersetze die bestehenden Release Notes (GitHub Actions, NPM Scripts für Cypress, Java 22 Update) eins zu eins ins Deutsche und behalte die Formatierung bei."*

## 6. UI & UX: Theme-Engine
* **Inhalt:** Nutzung der Vuetify Theme-Engine und `localStorage` zur Persistenz.
* **Sinngemäßer Prompt:** *"Erstelle eine Dokumentation für das Dark/Light Theme Feature basierend auf dem Screenshot. Erkläre die Technologie (Vuetify), die Implementierung in main.js/App.vue und die Persistenz-Logik mit Code-Snippet."*

## 7. UI & UX: Column Picker
* **Inhalt:** Reaktivität zwischen `v-select` und `v-data-table-server`.
* **Sinngemäßer Prompt:** *"Dokumentiere die dynamische Spaltenauswahl für die PWA. Erkläre die Trennung in allHeaders und selectedHeaders sowie die Einbindung des Vuetify Column Pickers inklusive Code-Beispiel."*

## 8. Security: API-Zugriffsschutz
* **Inhalt:** Implementierung eines `ApiGuard` Interceptors und `X-API-KEY` Header.
* **Sinngemäßer Prompt:** *"Fasse die Sicherheits-Implementierung für den API-Zugriffsschutz zusammen. Erkläre das Prinzip der Header-Authentifizierung, die Rolle des ApiGuards im Backend und die automatische Axios-Konfiguration im Frontend."*

## 9. PWA: Mobile Implementierung
* **Inhalt:** Konfiguration des `vite-plugin-pwa` und Meta-Tag-Optimierung.
* **Sinngemäßer Prompt:** *"Erstelle eine Kurz-Doku zur PWA-Implementierung. Zeige die Konfiguration in der vite.config.js (Manifest, Service Worker) und die notwendigen Meta-Tags in der index.html für das native App-Feeling."*

## 10. Infrastruktur: Deployment-Protokoll
* **Inhalt:** Swap-Speicher Einrichtung, Git-PAT und SCP-Transfer auf DigitalOcean.
* **Sinngemäßer Prompt:** *"Fasse das Deployment-Protokoll zusammen. Gehe auf die Cloud-Infrastruktur (DigitalOcean/Student Pack), das RAM-Problem (Swap-Lösung), den Code-Transfer via Token und den Daten-Upload via SCP ein. Erstelle dazu ein kompaktes Cheat Sheet."*

*Zuletzt aktualisiert: 09.01.2026*