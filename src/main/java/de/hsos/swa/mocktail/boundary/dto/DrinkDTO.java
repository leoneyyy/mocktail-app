package de.hsos.swa.mocktail.boundary.dto;

import de.hsos.swa.mocktail.entity.Drink;

import java.util.List;

public class DrinkDTO {
    public String id;
    public String name;
    public String zubereitung;
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
