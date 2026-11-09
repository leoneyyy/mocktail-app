package de.hsos.swa.mocktail.control;


import de.hsos.swa.mocktail.entity.MocktailKatalog;
import de.hsos.swa.mocktail.entity.MocktailNichtGefundenException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class LoescheMocktail {

@Inject
MocktailKatalog katalog;

public boolean loeschen(long id){
    if (!katalog.loesche(id)) {
        throw new MocktailNichtGefundenException(id);
    }
    return katalog.loesche(id);
}

}
