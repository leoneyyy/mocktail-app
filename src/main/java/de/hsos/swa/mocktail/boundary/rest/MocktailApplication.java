package de.hsos.swa.mocktail.boundary.rest;

import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Drink-API",
                version = "1.0.0",
                description = "Verwaltung eigener Mocktail-Rezepte und Suche nach Cocktails ueber TheCocktailDB. \"\n" +
                        "                        + \"Mocktails unter /drinks/mocktail, Cocktails unter /drinks/cocktail, \"\n" +
                        "                        + \"jeweils filterbar nach name und zutat.",

                contact = @Contact(name = "Leon Schlender und Vivien Prell") )


)

public class MocktailApplication extends Application {
}
