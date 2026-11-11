# Mocktail-App – Befehle zum Bauen, Starten und Testen

Alle Befehle im **Projektordner** ausführen (dort, wo die `pom.xml` liegt).
Statt `mvn` geht überall auch der Maven-Wrapper: `./mvnw` (Linux/WSL) bzw. `.\mvnw` (Windows).

---

## 1. Voraussetzungen prüfen

```bash
java -version      # Java 21 oder neuer
javac -version     # Compiler muss da sein (sonst: sudo apt install openjdk-21-jdk-headless)
mvn -v             # Maven 3.9 oder neuer, mit Java 21+
```

---

## 2. Maven-Befehle

| Befehl | Was passiert |
|---|---|
| `mvn compile` | Code kompilieren |
| `mvn test` | + alle Tests ausführen (RestAssured) |
| `mvn package` | + fertiges Programm bauen → `target/quarkus-app/quarkus-run.jar` |
| `mvn verify` | + Integrationstests (`...IT.java`) |
| `mvn install` | + Paket ins lokale Maven-Repository (`~/.m2`) kopieren |
| `mvn clean` | Ordner `target/` löschen (bei komischen Fehlern / alten Klassen) |
| `mvn clean test` | Sauber neu bauen und testen |

Jede Phase führt alle vorherigen mit aus (`package` = compile + test + package).

---

## 3. Starten

### Dev-Mode (zum Entwickeln)

```bash
mvn quarkus:dev
```

- Live Reload: Code speichern → beim nächsten Aufruf aktiv, kein Neustart nötig
- Taste **`r`** im Terminal: Tests laufen lassen
- Dev UI: `http://localhost:8080/q/dev-ui`
- Swagger UI: `http://localhost:8080/q/swagger-ui` (braucht Extension `smallrye-openapi`)
- Beenden: `q` oder **Strg+C**

### JVM-Mode (fertiges Programm)

```bash
mvn package
java -jar target/quarkus-app/quarkus-run.jar
```

- Profil `prod`, kein Live Reload, keine Dev UI
- Anderer Port: `java -Dquarkus.http.port=8085 -jar target/quarkus-app/quarkus-run.jar`
- Beenden: **Strg+C**
- Daten liegen nur im Arbeitsspeicher → nach Neustart nur die Beispieldaten

---

## 4. API benutzen mit curl

Läuft die App, in einem **zweiten Terminal**:

```bash
# Alle Mocktails (200)
curl http://localhost:8080/mocktails

# Filtern (200)
curl "http://localhost:8080/mocktails?zutat=minze"
curl "http://localhost:8080/mocktails?name=virgin"

# Einen Mocktail (200 / 404)
curl -i http://localhost:8080/mocktails/1

# Anlegen (201 + Location-Header / 400)
curl -i -X POST http://localhost:8080/mocktails \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Shirley Temple",
    "beschreibung": "süß und fruchtig",
    "zubereitung": "Ginger Ale auf Eis, Grenadine dazu",
    "zutaten": [
      { "name": "Ginger Ale", "menge": 200, "einheit": "ml" },
      { "name": "Grenadine",  "menge": 10,  "einheit": "ml" }
    ]
  }'

# Ändern (200 / 400 / 404)
curl -i -X PUT http://localhost:8080/mocktails/3 \
  -H "Content-Type: application/json" \
  -d '{"name":"Shirley Deluxe","beschreibung":"mit Kirsche","zubereitung":"auf Eis","zutaten":[{"name":"Ginger Ale","menge":200,"einheit":"ml"}]}'

# Löschen (204 / 404)
curl -i -X DELETE http://localhost:8080/mocktails/3

# Fehlerfall: ohne Name → 400
curl -i -X POST http://localhost:8080/mocktails \
  -H "Content-Type: application/json" \
  -d '{"name":"","zutaten":[]}'
```

`-i` zeigt Statuscode und Header (z. B. `Location`).
**Wichtig:** Die JSON-Schlüssel (`name`, `beschreibung`, `zubereitung`, `zutaten`, `menge`, `einheit`) müssen genau so heißen – nur die Werte ändern.

---

## 5. Alternative: HTTP-Client in IntelliJ

Datei `mocktail.http` im Projekt anlegen, grünen Pfeil neben einer Anfrage klicken:

---

## 6. Statuscodes der API

| Methode | URI | Erfolg | Fehler |
|---|---|---|---|
| GET | `/mocktails[?name=&zutat=]` | 200 | – |
| GET | `/mocktails/{id}` | 200 | 404 |
| POST | `/mocktails` | 201 + Location | 400 |
| PUT | `/mocktails/{id}` | 200 | 400, 404 |
| DELETE | `/mocktails/{id}` | 204 | 404 |

Weitere Codes beim Testen:

| Code | Bedeutung |
|---|---|
| 404 | Pfad/Ressource gibt es nicht |
| 405 | Pfad gibt es, aber nicht mit dieser HTTP-Methode (z. B. fehlendes `@GET @Path("{id}")`) |
| 415 | Falscher/fehlender `Content-Type` (kein `application/json`) |
| 500 | Exception im Server – meist fehlt ein ExceptionMapper oder ein Bug |

---


```

Pfade in Git Bash: `/c/Users/Leon/...` (mit `/`), nicht `C:\Users\Leon\...`.