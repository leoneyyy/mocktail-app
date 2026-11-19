package de.hsos.swa.cocktail.entity;

import java.util.List;

public interface DrinkKatalog {

    List<Drink> finde(String id);
    List<Drink> sucheNachName(String name);
    List<Drink> sucheNachKategorie(String alkohol);
    List<Drink> sucheNachZutat(String zutat);

}
