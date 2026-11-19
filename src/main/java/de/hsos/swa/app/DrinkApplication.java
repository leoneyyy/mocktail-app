package de.hsos.swa.app;

import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Drink-API",
                version = "2.0.0",
                description = "Verwaltung eigener Mocktail-Rezepte und Suche nach Cocktails ueber TheCocktailDB. "
                        + "Mocktails unter /drinks/mocktail, Cocktails unter /drinks/cocktail, "
                        + "jeweils filterbar nach name und zutat.",

                contact = @Contact(name = "Leon Schlender und Vivien Prell",
                email = "test",
                url = "https://github.com/leoneyyy") )


)

public class DrinkApplication extends Application {
}
