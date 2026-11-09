package de.hsos.swa.mocktail.control;

import de.hsos.swa.mocktail.entity.Mocktail;
import de.hsos.swa.mocktail.entity.MocktailKatalog;
import de.hsos.swa.mocktail.entity.Zutat;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
@ApplicationScoped
public class ErstelleMocktail  {

    @Inject
    MocktailKatalog katalog;


    public Mocktail erstelle(String name, String beschreibung, String zubereitung, List<Zutat> zutaten){
        return katalog.speichere(new Mocktail(name,beschreibung,zubereitung,zutaten));
    }

}

