package de.hsos.swa.mocktail.boundary.rest;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class MocktailResourceTest {

    private static final String JSON = """
            {
             "name": "Shirley Temple",
                          "beschreibung": "süß",
                          "zubereitung": "auf Eis",
                          "zutaten": [ { "name": "Ginger Ale", "menge": 200, "einheit": "ml" } ]
            }
            
            """;

    @Test
    void erstellen() {
        given().contentType(ContentType.JSON).body(JSON).when().post("/drinks/mocktail").then().statusCode(201).body("name", equalTo("Shirley Temple")).body("zutaten", hasSize(1));
    }

    @Test
    void aendern() {
        int id = given().contentType(ContentType.JSON).body(JSON).when().post("/drinks/mocktail").then().statusCode(201).extract().path("id");

        given().contentType(ContentType.JSON).body("""
                                {
                                  "name": "Shirley Deluxe",
                                  "beschreibung": "mit Kirsche",
                                  "zubereitung": "auf Eis",
                                  "zutaten": [ { "name": "Ginger Ale", "menge": 200, "einheit": "ml" } ]
                                }
                """).when().put("/drinks/mocktail/" + id).then().statusCode(200).body("name", equalTo("Shirley Deluxe"));

        given().when().get("/drinks/mocktail/" + id).then().statusCode(200).body("beschreibung", equalTo("mit Kirsche"));
    }

    @Test
    void alleMocktailsEnthaltenBeispieldaten() {
        given().when().get("/drinks/mocktail")
                .then().statusCode(200).body("name", hasItems("Virgin Mojito", "Virgin Colada"));
    }

    @Test
    void ungueltigeEingabeGibt400() {
        given().contentType(ContentType.JSON).body("{ \"name\": \"\", \"zutaten\": [] }")
                .when().post("/drinks/mocktail").then().statusCode(400).body("fehler", notNullValue());
    }

    @Test
    void sucheOhneTrefferGibtLeereListe() {
        given().queryParam("name", "gibtesnicht").when().get("/drinks/mocktail")
                .then().statusCode(200).body("size()", is(0));
    }

    @Test
    void filterNachZutat() {
        given().queryParam("zutat", "minze").when().get("/drinks/mocktail")
                .then().statusCode(200).body("name", hasItem("Virgin Mojito")).body("name", not(hasItem("Virgin Colada")));
    }

    @Test
    void filterNachName() {
        given().queryParam("name", "colada").when().get("/drinks/mocktail")
                .then().statusCode(200).body("name", contains("Virgin Colada"));
    }

    @Test
    void unbekannterMocktailGibt404() {
        given().when().get("/drinks/mocktail/999").then().statusCode(404).body("fehler", notNullValue());
    }
}
