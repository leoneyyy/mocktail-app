package de.hsos.swa.mocktail.boundary.dto;

import de.hsos.swa.mocktail.entity.Drink;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;


@Schema(description = "Cocktail von TheCocktailDB")
public class CocktailDTO {

    @Schema(description = "ID des Cocktails bei TheCocktailDB", examples = {"11000"})
    public String id;
    @Schema(description = "Name des Cocktails", examples = {"Mojito"})
    public String name;
    @Schema(description = "Zubereitung; bei der Suche nach Zutat oder ohne Filter leer, dann ueber /drinks/cocktail/{id} abrufen",
            examples = "Muddle mint leaves with sugar and lime juice.")
    public String zubereitung;
    @Schema(description = "Zutaten; bei der Suche nach Zutat oder ohne Filter leer, dann ueber /drinks/cocktail/{id} abrufen")
    public List<String> zutaten;

    public CocktailDTO(){}

    public static CocktailDTO von(Drink drink){
        CocktailDTO dto = new CocktailDTO();
        dto.id = drink.id();
        dto.name = drink.name();
        dto.zubereitung = drink.zubereitung();
        dto.zutaten = drink.zutaten();
        return dto;
    }
}
