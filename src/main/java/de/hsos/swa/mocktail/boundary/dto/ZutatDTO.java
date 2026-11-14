package de.hsos.swa.mocktail.boundary.dto;

import de.hsos.swa.mocktail.entity.Zutat;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description = "Eine Zutat mit Menge und Einheit")
public class ZutatDTO {
    @Schema(description = "Name der Zutat", examples = {"Minze"})
    public String name;
    @Schema(description = "Menge", examples = {"8"})
    public double menge;
    @Schema(description = "Einheit der Menge", examples = {"Blaetter"})
    public String einheit;

    public ZutatDTO() {}

    public ZutatDTO(String name, double menge, String einheit) {
        this.name = name;
        this.menge = menge;
        this.einheit = einheit;
    }

    public static ZutatDTO von(Zutat zutat){
        return new ZutatDTO(zutat.name(),zutat.menge(),zutat.einheit());
    }

    public Zutat zuZutat(){
        return new Zutat(name,menge,einheit);
    }

}
