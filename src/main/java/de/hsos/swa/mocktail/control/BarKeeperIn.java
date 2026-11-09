package de.hsos.swa.mocktail.control;

import de.hsos.swa.mocktail.entity.Mocktail;
import de.hsos.swa.mocktail.entity.Zutat;
import jakarta.inject.Inject;

import java.util.List;

public class BarKeeperIn implements MocktailAendern, MocktailErstellen, MocktailLoeschen, MocktailAbfragen {


    @Inject
    SucheMocktails sucheMocktails;

    @Inject
    AendereMocktail aendereMocktail;

    @Inject
    LoescheMocktail loescheMocktail;

    @Inject
    ErstelleMocktail erstelleMocktail;


    @Override
    public List<Mocktail> alle() {
        return sucheMocktails.;
    }

    @Override
    public List<Mocktail> suche(String name, String zutat) {
        return List.of();
    }

    @Override
    public Mocktail finde(long id) {
        return null;
    }

    @Override
    public Mocktail aendere(long id, String name, String beschreibung, String zubereitung, List<Zutat> zutaten) {
        return null;
    }

    @Override
    public Mocktail erstelle(String name, String beschreibung, String zubereitung, List<Zutat> zutaten) {
        return null;
    }

    @Override
    public void loeschen(long id) {

    }
}
