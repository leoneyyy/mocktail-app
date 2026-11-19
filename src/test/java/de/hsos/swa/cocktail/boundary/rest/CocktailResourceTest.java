package de.hsos.swa.cocktail.boundary.rest;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

// Achtung: diese Tests rufen die echte TheCocktailDB auf und brauchen Internet
@QuarkusTest
public class CocktailResourceTest {

    @Test
    void sucheNachNameLiefertCocktails() {
        given().queryParam("name", "mojito").when().get("/drinks/cocktail")
                .then().statusCode(200).body("name", hasItem("Mojito"));
    }

    @Test
    void sucheOhneTrefferGibtLeereListe() {
        given().queryParam("zutat", "gibtesnicht").when().get("/drinks/cocktail")
                .then().statusCode(200).body("size()", is(0));
    }

    @Test
    void cocktailNachId() {
        given().when().get("/drinks/cocktail/11000")
                .then().statusCode(200).body("name", equalTo("Mojito")).body("zutaten", not(empty()));
    }

    @Test
    void unbekannterCocktailGibt404() {
        given().when().get("/drinks/cocktail/99999999")
                .then().statusCode(404).body("fehler", notNullValue());
    }

    @Test
    void unbekannteKategorieGibt404() {
        given().when().get("/drinks/bier").then().statusCode(404);
    }
}
