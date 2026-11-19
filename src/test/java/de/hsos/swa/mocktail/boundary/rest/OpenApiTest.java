package de.hsos.swa.mocktail.boundary.rest;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class OpenApiTest {

    @Test
    void openApiEnthaeltInfoUndTags() {
        given().queryParam("format", "json").when().get("/q/openapi")
                .then().statusCode(200)
                .body("info.version", equalTo("2.0.0"))
                .body("tags.name", hasItems("Mocktails", "Cocktails"))
                .body("paths.'/drinks/mocktail'.get.tags", contains("Mocktails"))
                .body("paths.'/drinks/cocktail/{id}'.get.tags", contains("Cocktails"));
    }
}
