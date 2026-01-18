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
