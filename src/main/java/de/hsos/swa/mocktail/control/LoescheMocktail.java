package de.hsos.swa.mocktail.control;


import de.hsos.swa.mocktail.entity.MocktailKatalog;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class LoescheMocktail {

@Inject
MocktailKatalog katalog;

public boolean loeschen(long id){
    return katalog.loesche(id);
}

}
