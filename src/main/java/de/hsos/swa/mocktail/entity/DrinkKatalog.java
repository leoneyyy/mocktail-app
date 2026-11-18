package de.hsos.swa.mocktail.entity;

import java.util.List;

public interface DrinkKatalog {

    List<Drink> finde(String id);
    List<Drink> sucheNachName(String name);
    List<Drink> sucheNachKategorie(String alkohol);
    List<Drink> sucheNachZutat(String zutat);

}
