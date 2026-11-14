package de.hsos.swa.mocktail.boundary.rest;
import static org.hamcrest.Matchers.greaterThan;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
@QuarkusTest
public class DrinkResourceTest {

    @Test
    void sucheNachNameLiefertDrinks() {
        given().when().get("/drinks/name/mojito")
                .then().statusCode(200).body("size()", greaterThan(0));
    }

    @Test
    void unbekannteKategorieGibt400() {
        given().when().get("/drinks/kategorie/bier").then().statusCode(400);
    }
}
