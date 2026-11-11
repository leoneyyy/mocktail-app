package de.hsos.swa.mocktail.control;

import de.hsos.swa.mocktail.entity.Mocktail;
import de.hsos.swa.mocktail.entity.MocktailKatalog;
import de.hsos.swa.mocktail.entity.MocktailNichtGefundenException;
import io.quarkus.vertx.http.runtime.filters.accesslog.JBossLoggingAccessLogReceiver;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
@ApplicationScoped
public class SucheMocktails {


    @Inject
    MocktailKatalog katalog;

    public List<Mocktail> alle(){
        return katalog.alle();
    }

    public List<Mocktail> suche(String name, String zutat){
        List<Mocktail> ergebnis = new ArrayList<>();
        for (Mocktail m : katalog.alle()) {

            boolean namePasst = (name == null) || m.getName().toLowerCase().contains(name.toLowerCase());
            boolean zutatPasst = (zutat == null) || m.enthaeltZutat(zutat);

            if (namePasst && zutatPasst) {
                ergebnis.add(m);
            }
        }
        return ergebnis;
    }


    public Mocktail finde(long id){
        Mocktail mocktail = katalog.finde(id);
        if(mocktail == null){
            throw new MocktailNichtGefundenException(id);
        }
        return mocktail;
    }



}
