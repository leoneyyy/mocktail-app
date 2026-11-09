package de.hsos.swa.mocktail.boundary.dto;

import de.hsos.swa.mocktail.entity.Mocktail;

import java.util.ArrayList;
import java.util.List;

public class MocktailDTO {

    public long id;
    public String name;
    public String beschreibung;
    public String zubereitung;
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
