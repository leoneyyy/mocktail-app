package de.hsos.swa.cocktail.entity;

import java.util.List;

public record Drink(String id, String name, String zubereitung, List<String> zutaten) {

    public boolean enthaeltZutat(String zutat){
        for (String z : zutaten) {
            if (z.toLowerCase().contains(zutat.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
