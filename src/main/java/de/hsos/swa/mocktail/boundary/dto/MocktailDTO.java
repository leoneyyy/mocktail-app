package de.hsos.swa.mocktail.boundary.dto;

import de.hsos.swa.mocktail.entity.Mocktail;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import java.util.List;

@Schema(description = "Mocktail, wie ihn die API zurueckgibt")
public class MocktailDTO {

    @Schema(description = "Vom Server vegebene ID", examples ={"1"}, readOnly = true)
    public long id;
    @Schema(description = "Name des Mocktails", examples = {"Virgin Mojito"})
    public String name;
    @Schema(description = "Beschreibung des Mocktails", examples = {"Frisch mit Minze und Limette"})
    public String beschreibung;
    @Schema(description = "Zubereitung des Mocktails", examples = {"Minze zerdruecken, Eis dazu, mit Soda auffuellen"})
    public String zubereitung;
    @Schema(description = "Zutaten des Mocktails")
    public List<ZutatDTO> zutaten;

    public MocktailDTO(){}

    public static MocktailDTO von(Mocktail m){
        MocktailDTO dto = new MocktailDTO();
        dto.id = m.getId();
        dto.name = m.getName();
        dto.beschreibung = m.getBeschreibung();
        dto.zubereitung = m.getZubereitung();
        dto.zutaten = m.getZutaten().stream().map(ZutatDTO::von).toList();
        return dto;
    }
}
