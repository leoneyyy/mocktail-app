package de.hsos.swa.mocktail.control;

import de.hsos.swa.mocktail.entity.Mocktail;

import java.util.List;

public interface MocktailAbfragen {

    List<Mocktail> alle();
    List<Mocktail> suche(String name, String zutat);
    Mocktail finde(long id);
}
