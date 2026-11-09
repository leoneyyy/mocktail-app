package de.hsos.swa.mocktail.boundary.dto;

import de.hsos.swa.mocktail.entity.Zutat;

import java.util.List;

public class MocktailEingabeDTO {

    public String name;
    public String beschreibung;
    public String zubereitung;
    public List<ZutatDTO> zutaten;

    public MocktailEingabeDTO() {}

    public List<Zutat> zutatenAlsEntity(){
        if(zutaten == null){
            return List.of();
        }
        return zutaten.stream().map(ZutatDTO::zuZutat).toList();
    }
}
