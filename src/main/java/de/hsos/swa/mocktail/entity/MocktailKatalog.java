package de.hsos.swa.mocktail.entity;

import java.util.List;

public interface MocktailKatalog {

    List<Mocktail> alle();

    Mocktail finde(long id);

    List<Mocktail> finde(String name, String zutat);

    Mocktail speichere(Mocktail mocktail);

    boolean loesche(long id);

}
