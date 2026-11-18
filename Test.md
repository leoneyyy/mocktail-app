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

## 5. Alternative: HTTP-Client in IntelliJ

Datei `mocktail.http` im Projekt anlegen, grünen Pfeil neben einer Anfrage klicken:

---

## 6. Statuscodes der API

| Methode | URI | Erfolg | Fehler |
|---|---|---|---|
| GET | `/drinks/mocktail[?name=&zutat=]` | 200 | – |
| GET | `/drinks/mocktail/{id}` | 200 | 404 |
| POST | `/drinks/mocktail` | 201 + Location | 400 |
| PUT | `/drinks/mocktail/{id}` | 200 | 400, 404 |
| DELETE | `/drinks/mocktail/{id}` | 204 | 404 |
| GET | `/drinks/cocktail[?name=&zutat=]` | 200 | – |
| GET | `/drinks/cocktail/{id}` | 200 | 404 |

Weitere Codes beim Testen:

| Code | Bedeutung |
|---|---|
| 404 | Pfad/Ressource gibt es nicht |
| 405 | Pfad gibt es, aber nicht mit dieser HTTP-Methode (z. B. fehlendes `@GET @Path("{id}")`) |
| 415 | Falscher/fehlender `Content-Type` (kein `application/json`) |
| 500 | Exception im Server – meist fehlt ein ExceptionMapper oder ein Bug |

---
# Swagger

http://localhost:8080/q/swagger-ui

Pfade in Git Bash: `/c/Users/Leon/...` (mit `/`), nicht `C:\Users\Leon\...`.

---

## 7. Aufbau des Projekts (Boundary – Entity – Gateway)

| Paket | Inhalt | Warum |
|---|---|---|
| `boundary.rest` | Resource-Klassen, ExceptionMapper | Die REST-Schnittstelle nach außen, kennt HTTP |
| `boundary.dto` | `MocktailDTO`, `MocktailEingabeDTO`, `ZutatDTO`, `CocktailDTO` | Was über die Leitung geht, ist getrennt von den Entities. Der Client kann z. B. keine `id` setzen |
| `entity` | `Mocktail`, `Zutat`, `Drink`, Interfaces `MocktailKatalog`, `DrinkKatalog` | Fachlogik und Validierung, kennt weder HTTP noch Datenbank |
| `gateway.memory` | `MocktailRepository` | Speichert die eigenen Mocktails im Arbeitsspeicher |
| `gateway.cocktaildb` | `CocktailDbRepository`, `CocktailDbClient`, `CocktailDbAntwort` | Anbindung an TheCocktailDB |

Die Resources hängen nur von den **Interfaces** im `entity`-Paket ab (`MocktailKatalog`, `DrinkKatalog`).
Welche Implementierung dahintersteckt, entscheidet CDI. So kann man z. B. das Memory-Repository
später durch eine Datenbank ersetzen, ohne die Resources anzufassen.

Jeder Pfad hat eine eigene Resource-Klasse:

| Pfad | Klasse |
|---|---|
| `/drinks/mocktail` | `MocktailResource` |
| `/drinks/mocktail/{id}` | `MocktailIdResource` |
| `/drinks/cocktail` | `CocktailResource` |
| `/drinks/cocktail/{id}` | `CocktailIdResource` |

---

## 8. Annotationen – was sie tun und warum

### 8.1 JAX-RS (REST-Schnittstelle, `jakarta.ws.rs`)

| Annotation | Wo | Was / Warum |
|---|---|---|
| `@Path("/drinks/mocktail")` | Klasse | Unter welcher URI die Resource erreichbar ist. Bei `{id}` wird ein Teil der URI zum Parameter |
| `@GET` `@POST` `@PUT` `@DELETE` | Methode | Welche HTTP-Methode die Methode bedient. Fehlt sie für einen Pfad → **405** |
| `@Produces(APPLICATION_JSON)` | Klasse | Antworten werden als JSON geschrieben (JSON-B wandelt die DTOs um) |
| `@Consumes(APPLICATION_JSON)` | Klasse | Request-Body muss JSON sein, sonst **415** |
| `@PathParam("id")` | Parameter | Holt `{id}` aus der URI. Bei `long id` und `/drinks/mocktail/abc` → automatisch **404** |
| `@QueryParam("name")` | Parameter | Holt `?name=...`. Fehlt er, ist der Wert `null` → deshalb die `null`-Prüfungen beim Filtern |
| `@Context UriInfo` | Parameter | Infos über den aktuellen Request; damit wird beim POST der `Location`-Header (`/drinks/mocktail/3`) gebaut |
| `@Provider` | Klasse | Meldet einen `ExceptionMapper` bei JAX-RS an |

**ExceptionMapper:** Statt in jeder Methode `Response.status(404)...` zu bauen, wirft die Methode
eine Exception, und ein Mapper macht daraus die passende Antwort. Das hält die Resources kurz und das
Fehlerformat einheitlich (`{"fehler": "..."}`).

| Mapper | Exception | Status |
|---|---|---|
| `EingabeFehlerMapper` | `IllegalArgumentException` (z. B. aus `Mocktail.aendere` oder `Zutat`) | 400 |
| `FehlerMapper` | `MocktailNichtGefundenException` | 404 |
| `NichtGefundenMapper` | `jakarta.ws.rs.NotFoundException` (unbekannter Cocktail, unbekannter Pfad) | 404 |

Ohne passenden Mapper würde aus einer Exception eine **500**.

**Resources ohne Scope-Annotation:** In Quarkus sind Resource-Klassen standardmäßig `@Singleton`,
d. h. es gibt eine Instanz für alle Requests. Deshalb dürfen sie keinen Zustand in Feldern halten
(außer den injizierten Abhängigkeiten).

### 8.2 CDI (Dependency Injection, `jakarta.inject` / `jakarta.enterprise`)

| Annotation | Wo | Was / Warum |
|---|---|---|
| `@ApplicationScoped` | `MocktailRepository`, `CocktailDbRepository` | Eine Instanz für die ganze Anwendung. Wichtig fürs Repository: alle Requests sehen dieselbe Map. Außerdem bekommt die Bean einen Proxy, über den die Fault-Tolerance-Annotationen erst funktionieren |
| `@Inject` | Feld | CDI setzt die passende Implementierung ein. Gefragt wird nach dem **Interface** (`MocktailKatalog`), geliefert wird `MocktailRepository` |
| `@PostConstruct` | `beispieldaten()` | Läuft einmal, nachdem CDI die Bean erzeugt hat → legt die Beispiel-Mocktails an |

### 8.3 REST-Client (Aufruf von TheCocktailDB)

| Annotation | Wo | Was / Warum |
|---|---|---|
| `@RegisterRestClient(configKey = "cocktaildb")` | `CocktailDbClient` | Quarkus erzeugt aus dem Interface selbst einen HTTP-Client. Die Basis-URL kommt aus `quarkus.rest-client.cocktaildb.url` in `application.properties` – so steht keine URL im Code |
| `@GET @Path("/search.php") @QueryParam("s")` | Interface-Methoden | Gleiche Annotationen wie bei den Resources, nur andersherum: beschreiben den Aufruf, z. B. `GET .../search.php?s=mojito` |
| `@RestClient` | beim `@Inject` | Qualifier: „gib mir den generierten REST-Client“ |

`CocktailDbAntwort.drinks` ist absichtlich `Object` und keine Liste: TheCocktailDB liefert bei
keinem Treffer `null` (`search.php`, `lookup.php`) oder den String `"no data found"` (`filter.php`).
Mit `List` würde das Einlesen fehlschlagen.

### 8.4 Fault Tolerance (`org.eclipse.microprofile.faulttolerance`)

Die CocktailDB ist ein fremder Dienst, der langsam oder weg sein kann. Damit unsere API trotzdem
antwortet, sind die Methoden in `CocktailDbRepository` abgesichert:

| Annotation | Bedeutung |
|---|---|
| `@Timeout(3000)` | Ein Aufruf darf höchstens 3 s dauern, sonst gilt er als fehlgeschlagen |
| `@Retry(maxRetries = 2, delay = 500)` | Bei Fehler bis zu 2-mal wiederholen (also max. 3 Versuche), 500 ms Pause dazwischen |
| `@CircuitBreaker(requestVolumeThreshold = 4, failureRatio = 0.5, delay = 10000)` | Von den letzten 4 Aufrufen sind mind. 50 % fehlgeschlagen → Schalter „offen“: 10 s lang wird die CocktailDB gar nicht mehr gefragt, sondern sofort abgebrochen. Danach ein Probeaufruf („halb offen“) |
| `@Fallback(fallbackMethod = "keineDrinks")` | Wenn am Ende alles fehlschlägt, wird diese Methode aufgerufen (leere Liste bzw. `Optional.empty()`). Sie muss **dieselben Parameter und denselben Rückgabetyp** haben |

Reihenfolge von außen nach innen: **Fallback → Retry → CircuitBreaker → Timeout**.
Jeder einzelne Retry-Versuch zählt also beim CircuitBreaker mit. Deshalb ist es wichtig, dass
„keine Treffer“ **kein** Fehler ist – sonst würden normale Suchen den Schalter öffnen.

### 8.5 OpenAPI / Swagger (`org.eclipse.microprofile.openapi`)

Diese Annotationen ändern **nichts am Verhalten**, sie beschreiben die API für
`/q/openapi` und die Swagger UI.

| Annotation | Wo | Was |
|---|---|---|
| `@OpenAPIDefinition(info = @Info(...))` | `MocktailApplication` | Titel, Version, Beschreibung der ganzen API. Die Klasse `extends Application` ist in Quarkus nicht nötig, sie dient nur als Träger dieser Annotation |
| `@Operation(summary, description)` | Methode | Überschrift und Text des Endpunkts |
| `@APIResponses` / `@APIResponse(responseCode, description, content)` | Methode | Welche Statuscodes vorkommen können und was zurückkommt |
| `@Content(schema = @Schema(implementation = MocktailDTO.class))` | in `@APIResponse` | Welcher Typ im Body steht. `type = SchemaType.ARRAY` → eine Liste davon |
| `@Parameter(description, example)` | Parameter | Beschreibung von Pfad-/Query-Parametern |
| `@Schema(description, examples, required, readOnly)` | DTO-Klasse/-Feld | Beschreibung der Felder. `readOnly` bei `id`: nur in Antworten. **Achtung:** `required = true` ist nur Doku, geprüft wird in `Mocktail.aendere()` und `Zutat` |

### 8.6 Tests

| Annotation | Was / Warum |
|---|---|
| `@QuarkusTest` | Startet die echte Anwendung (Profil `test`, Port **8081**) einmal für alle Testklassen. RestAssured schickt echte HTTP-Requests dagegen |
| `@Test` | JUnit-5-Testmethode |

Da die Anwendung nur einmal startet, teilen sich alle Tests das Repository: angelegte Mocktails
bleiben für die folgenden Tests bestehen. Tests sollten sich deshalb nicht auf feste IDs (außer
1 und 2 aus den Beispieldaten) oder eine feste Anzahl verlassen.
`CocktailResourceTest` ruft die echte TheCocktailDB auf und braucht Internet.

### 8.7 Java-Sprachmittel

- **`record`** (`Zutat`, `Drink`): unveränderliche Datenklasse; Konstruktor, Getter (`name()`),
  `equals`/`hashCode`/`toString` werden automatisch erzeugt.
- **Kompakter Konstruktor** in `Zutat` (`public Zutat { ... }`): prüft die Werte, bevor sie gesetzt
  werden → eine ungültige Zutat kann gar nicht entstehen.
- **`Optional<Drink>`** bei `DrinkKatalog.finde`: macht im Typ sichtbar, dass es den Cocktail
  vielleicht nicht gibt, statt `null` zurückzugeben.

---

## 9. Logging

### 9.1 Logger im Code

```java
private static final Logger LOG = Logger.getLogger(MocktailResource.class);
```

- `org.jboss.logging.Logger` ist der Logger, den Quarkus selbst benutzt.
- `static final`: ein Logger pro Klasse, nicht pro Objekt.
- Der Klassenname wird zur **Kategorie** (`de.hsos.swa.mocktail.boundary.rest.MocktailResource`).
  Über die Kategorie kann man in `application.properties` das Level pro Paket einstellen.

**`debugf("... %d", id)` statt `debug("..." + id)`:** Bei den `...f`-Methoden wird der Text nur
zusammengebaut, wenn das Level auch aktiv ist. Bei `+` würde der String immer erzeugt, auch wenn
er gar nicht ausgegeben wird.

### 9.2 Die Log-Level

Die Level sind nach Wichtigkeit geordnet. Stellt man ein Level ein, wird **dieses und alles
Wichtigere** ausgegeben. Bei `INFO` erscheinen also `INFO`, `WARN`, `ERROR` und `FATAL`,
aber kein `DEBUG` und kein `TRACE`.

```
TRACE  <  DEBUG  <  INFO  <  WARN  <  ERROR  <  FATAL
 (am meisten, unwichtigste)              (am wenigsten, wichtigste)
```

| Level | Methode | Wofür allgemein | Frage, die man sich stellt |
|---|---|---|---|
| `TRACE` | `LOG.trace` / `tracef` | Feinste Einzelschritte im Inneren: welche Methode mit welchen Werten aufgerufen wurde | „Was passiert hier Schritt für Schritt?“ |
| `DEBUG` | `LOG.debug` / `debugf` | Ablauf auf grober Ebene: welcher Request kam rein, was wurde gesucht, wie viele Treffer | „Was hat die Anwendung gerade gemacht?“ |
| `INFO` | `LOG.info` / `infof` | Wichtige, normale Ereignisse: Start, Daten angelegt/geändert/gelöscht | „Was soll man auch im Betrieb sehen?“ |
| `WARN` | `LOG.warn` / `warnf` | Etwas Unerwartetes, die Anwendung läuft aber weiter | „Sollte sich das jemand ansehen?“ |
| `ERROR` | `LOG.error` / `errorf` | Ein Fehler, durch den eine Aktion nicht ausgeführt werden konnte | „Ist hier etwas kaputt?“ |
| `FATAL` | `LOG.fatal` / `fatalf` | Die Anwendung kann nicht weiterlaufen (z. B. Start schlägt fehl) | „Ist alles kaputt?“ |

Zusätzlich gibt es für die Konfiguration noch `ALL` (alles ausgeben) und `OFF` (nichts ausgeben).
Diese Werte schreibt man nur in `application.properties`, im Code ruft man sie nicht auf.

**Faustregel:** Je höher das Level, desto seltener sollte die Meldung vorkommen und desto eher
muss jemand etwas tun. Im Betrieb läuft man meist mit `INFO`. `DEBUG` und `TRACE` schaltet man nur
zur Fehlersuche ein, weil sie sehr viele Zeilen erzeugen.

### 9.3 Wo im Projekt welches Level steht – und warum genau dort

**`TRACE` – im `MocktailRepository`**

```java
LOG.tracef("finde() id=%d", id);
LOG.tracef("speichere id=%d", mitId.getId());
LOG.tracef("loesche() id=%d", id);
```

Das Repository ist die unterste Schicht. Jede Anfrage geht hier durch, oft mehrmals
(ein PUT ruft z. B. `finde` **und** `speichere` auf). Diese Zeilen braucht man nur, wenn man
genau verfolgen will, was im Speicher passiert – etwa wenn eine ID falsch vergeben wird.
Im normalen Betrieb wären sie nur Rauschen, deshalb das niedrigste Level.

**`DEBUG` – am Anfang jeder Resource-Methode und im `CocktailDbRepository`**

```java
LOG.debugf("GET /drinks/mocktail/%d", id);
LOG.debugf("%d Mocktails gefunden", mocktails.size());
LOG.debugf("CocktailDB: suche name=%s", name);
```

Hier sieht man, **welcher Request ankam** und was er bewirkt hat. Beim Entwickeln ist das
sehr hilfreich („Kommt mein Request überhaupt an? Mit welchem Parameter?“). Ein reiner
Lesezugriff (GET) ändert aber nichts, im Betrieb muss das niemand sehen → `DEBUG`, nicht `INFO`.
Auch der Aufruf der CocktailDB ist `DEBUG`: Man will beim Testen sehen, was an die fremde API
geschickt wird, im Betrieb wären es aber zu viele Zeilen.

**`INFO` – nach erfolgreichem Anlegen, Ändern und Löschen**

```java
LOG.infof("Mocktail %d '%s' erstellt", neuerMocktail.getId(), neuerMocktail.getName());
LOG.infof("Mocktail %d '%s' geaendert", gespeichert.getId(), gespeichert.getName());
LOG.infof("Mocktail %d gelöscht", id);
```

Das sind **Änderungen am Datenbestand**. Die will man auch im Betrieb nachvollziehen können
(„Wann wurde Mocktail 3 gelöscht?“). Sie passieren deutlich seltener als Lesezugriffe,
das Log bleibt also übersichtlich. Geloggt wird erst **nach** dem erfolgreichen Speichern –
sonst stünde „erstellt“ im Log, obwohl die Validierung danach noch fehlschlägt.
Auch Quarkus selbst loggt mit `INFO` (z. B. `mocktail-app ... started in 3.5s`).

**`WARN` – in den ExceptionMappern und im Fallback**

```java
LOG.warnf("400 bad Request: %s", meldung);                    // EingabeFehlerMapper
LOG.warnf("404 Not found: %s", exception.getMessage());       // FehlerMapper, NichtGefundenMapper
LOG.warnf("CocktailDB nicht erreichbar ..., Fallback fuer '%s'", suchtext);  // CocktailDbRepository
```

- **400 / 404:** Der Request konnte nicht ausgeführt werden, aber die Anwendung hat sich korrekt
  verhalten – der **Client** hat etwas Falsches geschickt. Das ist kein Fehler unseres Servers,
  daher nicht `ERROR`. Häufen sich solche Meldungen, ist aber vielleicht ein Client falsch
  programmiert oder die API-Doku unklar → deshalb trotzdem sichtbar als `WARN`.
- **Fallback:** Die CocktailDB antwortet nicht, unsere API liefert trotzdem eine (leere) Antwort.
  Die Anwendung läuft also weiter, aber die Nutzer bekommen schlechtere Ergebnisse. Genau das ist
  der Fall für `WARN`: nichts ist kaputt, aber jemand sollte es wissen.

**`ERROR` und `FATAL` – im Projekt nicht verwendet**

Es gibt im Moment keine Stelle, an der unser Server selbst kaputtgeht und das abfängt.
Unerwartete Exceptions (Bugs) fängt kein eigener Mapper; die loggt Quarkus selbst als `ERROR`
und antwortet mit 500. `ERROR` wäre z. B. richtig, wenn man später eine Datenbank anbindet und
das Speichern fehlschlägt. `FATAL` braucht man in einer Quarkus-Anwendung praktisch nie, weil
Quarkus Startfehler selbst meldet.

**Zusammengefasst:**

| Situation im Projekt | Level | Begründung in einem Satz |
|---|---|---|
| Methode im Repository aufgerufen | `TRACE` | Innere Details, nur zur gezielten Fehlersuche |
| Request kommt an / Suche ausgeführt / CocktailDB angefragt | `DEBUG` | Ablauf beim Entwickeln verfolgen, im Betrieb uninteressant |
| Mocktail angelegt, geändert, gelöscht | `INFO` | Datenänderung, die man auch später nachvollziehen will |
| Ungültige Eingabe (400), nicht gefunden (404) | `WARN` | Fehler des Clients, Server läuft korrekt weiter |
| CocktailDB nicht erreichbar → Fallback | `WARN` | Eingeschränkt, aber nicht kaputt |
| Unerwarteter Fehler im Server (500) | `ERROR` | Loggt Quarkus automatisch |

### 9.4 Konfiguration (`application.properties`)

```properties
quarkus.log.min-level=TRACE
quarkus.log.category."de.hsos.swa".level=TRACE

quarkus.log.console.level=INFO
quarkus.log.console.format=%d{HH:mm:ss.SSS} %-5p [%c{3.}] (%t) %s%e%n

quarkus.log.file.enabled=true
quarkus.log.file.level=TRACE
quarkus.log.file.path=logs/mocktail.log
quarkus.log.file.format=%d{yyyy-MM-dd HH:mm:ss.SSS} %-5p [%c] (%t) %s%e%n
```

| Einstellung | Bedeutung / Warum |
|---|---|
| `quarkus.log.min-level=TRACE` | Wird **beim Bauen** ausgewertet: Log-Aufrufe unter diesem Level entfernt Quarkus komplett. Standard ist `DEBUG` – ohne diese Zeile würden die `trace`-Aufrufe nie erscheinen, egal was man sonst einstellt |
| `category."de.hsos.swa".level=TRACE` | Für unsere eigenen Pakete alles erzeugen. Fremde Bibliotheken bleiben beim Standard (`INFO`), sonst wird das Log unlesbar |
| `console.level=INFO` | Auf der Konsole nur `INFO` und höher → übersichtlich |
| `file.level=TRACE` | In die Datei kommt alles → zum Nachlesen bei Fehlern |
| `file.path=logs/mocktail.log` | Ziel-Datei. Quarkus rotiert sie standardmäßig bei 10 MB und behält 5 alte Dateien (`mocktail.log.1` … `.5`). Der Ordner `logs/` steht in der `.gitignore` |

Ein Log-Eintrag erscheint nur, wenn er **alle** Stufen passiert:
min-level (Bauzeit) → Level der Kategorie → Level des Handlers (Konsole / Datei).
Beispiel: Ein `debug` aus `MocktailResource` landet in der Datei, aber nicht auf der Konsole.

**Format-Platzhalter:**

| Platzhalter | Bedeutung |
|---|---|
| `%d{HH:mm:ss.SSS}` | Zeitstempel |
| `%-5p` | Level, linksbündig auf 5 Zeichen (`INFO `, `WARN `) |
| `%c` / `%c{3.}` | Kategorie; `{3.}` kürzt jeden Paketnamen auf 3 Buchstaben → `de.hso.swa.moc.bou.res.MocktailResource` |
| `%t` | Thread (`executor-thread-1`) |
| `%s` | die eigentliche Nachricht |
| `%e` | Stacktrace, falls eine Exception mitgeloggt wurde |
| `%n` | Zeilenumbruch |

**Level beim Start ändern**, ohne die Datei anzufassen:

```bash
java -Dquarkus.log.console.level=DEBUG -jar target/quarkus-app/quarkus-run.jar
```



