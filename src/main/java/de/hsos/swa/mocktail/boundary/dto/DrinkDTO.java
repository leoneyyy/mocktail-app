package de.hsos.swa.mocktail.boundary.dto;

import de.hsos.swa.mocktail.entity.Drink;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;


@Schema(description = "Drink wie ihn die API zurück gibt")
public class DrinkDTO {

    @Schema(description = "ID des Drinks bei TheCocktailDB", examples = {"11000"})
    public String id;
    @Schema(description = "Name des Drinks", examples = {"Mojito"})
    public String name;
    @Schema(description = "Zubereitung, nur bei der Namenssuche gefuellt",
            examples = "Muddle mint leaves with sugar and lime juice.")
    public String zubereitung;
    @Schema(description = "Zutaten, nur bei der Namenssuche gefuellt")
    public List<String> zutaten;

    public DrinkDTO(){}

    public static DrinkDTO von(Drink drink){
        DrinkDTO dto = new DrinkDTO();
        dto.id = drink.id();
        dto.name = drink.name();
        dto.zubereitung = drink.zubereitung();
        dto.zutaten = drink.zutaten();
        return dto;

    }
}
