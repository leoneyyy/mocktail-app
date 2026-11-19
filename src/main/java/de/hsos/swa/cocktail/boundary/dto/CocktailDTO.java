package de.hsos.swa.cocktail.boundary.dto;

import de.hsos.swa.cocktail.entity.Drink;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;


@Schema(description = "Cocktail von TheCocktailDB")
public record CocktailDTO(
        @Schema(description = "ID des Cocktails bei TheCocktailDB", examples = {"11000"})
        String id,
        @Schema(description = "Name des Cocktails", examples = {"Mojito"})
        String name,
        @Schema(description = "Zubereitung; bei der Suche nach Zutat oder ohne Filter leer, dann ueber /drinks/cocktail/{id} abrufen",
                examples = "Muddle mint leaves with sugar and lime juice.")
        String zubereitung,
        @Schema(description = "Zutaten; bei der Suche nach Zutat oder ohne Filter leer, dann ueber /drinks/cocktail/{id} abrufen")
        List<String> zutaten
) {

    public static CocktailDTO von(Drink drink){
        return new CocktailDTO(drink.id(), drink.name(), drink.zubereitung(), drink.zutaten());
    }
}
