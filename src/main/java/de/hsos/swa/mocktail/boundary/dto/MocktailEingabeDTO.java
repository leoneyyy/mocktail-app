package de.hsos.swa.mocktail.boundary.dto;

import de.hsos.swa.mocktail.entity.Zutat;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;
@Schema(description = "Eingabedaten für ein Mocktail-Rezept")
public class MocktailEingabeDTO {

    @Schema(description = "Name des Mocktails",examples = {"Virgin Mojito"}, required = true)
    public String name;
    @Schema(description = "Kurze Beschreibung des Mocktails", examples = {"Frisch mit Minze und Limette"})
    public String beschreibung;
    @Schema(description = "Anleitung für Mocktail-Rezept", examples = {"Minze zerdrücken, Eis dazu, mit Soda auffüllen"})
    public String zubereitung;
    @Schema(description = "Auflistung der verwendeten Zutaten", required = true)
    public List<ZutatDTO> zutaten;


    public MocktailEingabeDTO() {}

    public List<Zutat> zutatenAlsEntity(){
        if(zutaten == null){
            return List.of();
        }
        return zutaten.stream().map(ZutatDTO::zuZutat).toList();
    }
}
