package de.hsos.swa.mocktail.boundary.dto;

import de.hsos.swa.mocktail.entity.Mocktail;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import java.util.List;

@Schema(description = "Mocktail, wie ihn die API zurueckgibt")
public record MocktailDTO(
        @Schema(description = "Vom Server vegebene ID", examples ={"1"}, readOnly = true)
        long id,
        @Schema(description = "Name des Mocktails", examples = {"Virgin Mojito"})
        String name,
        @Schema(description = "Beschreibung des Mocktails", examples = {"Frisch mit Minze und Limette"})
        String beschreibung,
        @Schema(description = "Zubereitung des Mocktails", examples = {"Minze zerdruecken, Eis dazu, mit Soda auffuellen"})
        String zubereitung,
        @Schema(description = "Zutaten des Mocktails")
        List<ZutatDTO> zutaten
) {

    public static MocktailDTO von(Mocktail m){
        return new MocktailDTO(
                m.getId(),
                m.getName(),
                m.getBeschreibung(),
                m.getZubereitung(),
                m.getZutaten().stream().map(ZutatDTO::von).toList());
    }
}
