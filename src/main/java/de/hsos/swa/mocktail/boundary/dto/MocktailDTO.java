package de.hsos.swa.mocktail.boundary.dto;

import de.hsos.swa.mocktail.entity.Mocktail;

import java.util.List;

public class MocktailDTO {

    public long id;
    public String name;
    public String beschreibung;
    public String zubereitung;
    List<ZutatDTO> zutaten;

    public MocktailDTO(){}

    public static MocktailDTO von(Mocktail m){
        MocktailDTO dto = new MocktailDTO();
        return dto;
    }
}
