package de.hsos.swa.mocktail.control;

import de.hsos.swa.mocktail.entity.Mocktail;
import de.hsos.swa.mocktail.entity.MocktailKatalog;
import de.hsos.swa.mocktail.entity.MocktailNichtGefundenException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Comparator;
import java.util.List;
@ApplicationScoped
public class SucheMocktails {


    @Inject
    MocktailKatalog katalog;

    public List<Mocktail> alle(){
        return katalog.alle().stream().sorted(Comparator.comparingLong(Mocktail::getId)).toList();
    }

    public List<Mocktail> suche(String name, String zutat){
        return alle().stream().filter(mocktail -> istLeer(name) || mocktail.getName().toLowerCase().contains(name.toLowerCase()))
                .filter(mocktail -> istLeer(zutat) || mocktail.enthaeltZutat(zutat)).toList();
    }

    private static boolean istLeer(String text){
        return text == null || text.isBlank();
    }

    public Mocktail finde(long id){
        Mocktail mocktail = katalog.finde(id);
        if(mocktail == null){
            throw new MocktailNichtGefundenException(id);
        }
        return mocktail;
    }



}
