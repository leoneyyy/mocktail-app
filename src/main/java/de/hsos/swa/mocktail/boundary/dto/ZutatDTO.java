package de.hsos.swa.mocktail.boundary.dto;

import de.hsos.swa.mocktail.entity.Zutat;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description = "Eine Zutat mit Menge und Einheit")
public record ZutatDTO(
        @Schema(description = "Name der Zutat", examples = {"Minze"})
        String name,
        @Schema(description = "Menge", examples = {"8"})
        double menge,
        @Schema(description = "Einheit der Menge", examples = {"Blaetter"})
        String einheit
) {

    public static ZutatDTO von(Zutat zutat){
        return new ZutatDTO(zutat.name(), zutat.menge(), zutat.einheit());
    }

    public Zutat zuZutat(){
        return new Zutat(name, menge, einheit);
    }
}
