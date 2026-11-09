package de.hsos.swa.mocktail.boundary.rest;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

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
        given().contentType(ContentType.JSON).body(JSON).when().post("/mocktails").then().statusCode(201).body("name", equalTo("Shirley Temple")).body("zutaten", hasSize(1));
    }

    @Test
    void aendern() {
        int id = given().contentType(ContentType.JSON).body(JSON).when().post("/mocktails").then().statusCode(201).extract().path("id");

        given().contentType(ContentType.JSON).body("""
                                {
                                  "name": "Shirley Deluxe",
                                  "beschreibung": "mit Kirsche",
                                  "zubereitung": "auf Eis",
                                  "zutaten": [ { "name": "Ginger Ale", "menge": 200, "einheit": "ml" } ]
                                }
                """).when().put("/mocktails/" + id).then().statusCode(200).body("name", equalTo("Shirley Deluxe"));

        given().when().get("/mocktails/" + id).then().statusCode(200).body("beschreibung", equalTo("mit Kirsche"));
    }
}
