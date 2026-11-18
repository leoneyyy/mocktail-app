package de.hsos.swa.mocktail.entity;

import java.util.List;
import java.util.Optional;

public interface DrinkKatalog {

    Optional<Drink> finde(String id);
    List<Drink> sucheNachName(String name);
    List<Drink> sucheNachKategorie(String alkohol);
    List<Drink> sucheNachZutat(String zutat);

}
