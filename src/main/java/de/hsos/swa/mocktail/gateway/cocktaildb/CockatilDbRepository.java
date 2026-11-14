package de.hsos.swa.mocktail.gateway.cocktaildb;

import de.hsos.swa.mocktail.entity.Drink;
import de.hsos.swa.mocktail.entity.DrinkKatalog;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class CockatilDbRepository implements DrinkKatalog {

    private static final Logger LOG = Logger.getLogger(CockatilDbRepository.class);


    @Inject
    @RestClient
    CocktailDbClient client;

    @Override
    public List<Drink> sucheNachName(String name) {
        LOG.debugf("CocktailDB: suche name=%s", name);
        try {
            return uebersetze(client.sucheNachName(name));
        } catch (RuntimeException e) {
            LOG.warnf("CocktailDB: keine lesbare Antwort fuer name=%s", name);
            return new ArrayList<>();
        }
    }

    @Override
    public List<Drink> sucheNachKategorie(String  alkohol) {
        LOG.debugf("CocktailDB: filter alkoholfrei e=%s", alkohol);
        try {
            return uebersetze(client.filterNachAlkohol(alkohol));
        } catch (RuntimeException e) {
            LOG.warnf("CcktailDB: Keine lesbare Antwort fuer Kategorie=%s", alkohol);
            return new ArrayList<>();
        }
        }


    @Override
    public List<Drink> sucheNachZutat(String zutat) {
        LOG.debugf("CocktailDB: suche Zutat=%s",zutat);
        try {
            return uebersetze(client.sucheNachZutat(zutat));
        }catch(RuntimeException e) {
            LOG.warnf("CocktailDB: Keine lesbare Antwort fuer zutat =%s", zutat);
            return new ArrayList<>();
        }
    }

    private List<Drink> uebersetze(CocktailDbAntwort antwort){
        List<Drink> drinks = new ArrayList<>();
        if(antwort == null ||antwort.drinks == null){
            return drinks;
        }
        for(Map<String,String> d: antwort.drinks){
            List<String> zutaten = new ArrayList<>();
            for (int i = 1; i <= 15 ; i++) {
                String zutat = d.get("strIngredient" + i);
                if(zutat != null){
                    zutaten.add(zutat);
                }
            }
            drinks.add(new Drink(d.get("idDrink"), d.get("strDrink"),d.get("strInstructions"),zutaten));
        }

        return drinks;
    }
}
