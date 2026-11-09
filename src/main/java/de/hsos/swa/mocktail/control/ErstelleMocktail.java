package de.hsos.swa.mocktail.control;

import de.hsos.swa.mocktail.entity.Mocktail;

import java.util.List;

public class ErstelleMocktail implements MocktailAbfragen {
    @Override
    public List<Mocktail> alle() {
        return List.of();
    }

    @Override
    public List<Mocktail> suche(String name, String zutat) {
        return List.of();
    }

    @Override
    public Mocktail finde(long id) {
        return null;
    }
}
