package de.hsos.swa.mocktail.control;

import de.hsos.swa.mocktail.entity.Mocktail;
import de.hsos.swa.mocktail.entity.MocktailKatalog;
import de.hsos.swa.mocktail.entity.MocktailNichtGefundenExeption;
import de.hsos.swa.mocktail.entity.Zutat;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
@ApplicationScoped
public class AendereMocktail {

    @Inject
    MocktailKatalog katalog;

    public Mocktail aendere(long id, String name, String beschreibung, String zubereitung, List<Zutat> zutaten){
        Mocktail mocktail = katalog.finde(id);
        if(mocktail == null){
            throw new MocktailNichtGefundenExeption(id);
        }
        mocktail.aendere(name,beschreibung,zubereitung,zutaten);

        return katalog.speichere(mocktail);

    }

}
