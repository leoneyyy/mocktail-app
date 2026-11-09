package de.hsos.swa.mocktail.control;

import de.hsos.swa.mocktail.entity.Mocktail;
import de.hsos.swa.mocktail.entity.Zutat;

import java.util.List;

public interface MocktailErstellen {
    Mocktail erstelle(String name, String beschreibung, String zubereitung, List<Zutat> zutaten);
}
