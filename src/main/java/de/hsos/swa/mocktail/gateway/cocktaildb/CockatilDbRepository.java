package de.hsos.swa.mocktail.gateway.cocktaildb;

import de.hsos.swa.mocktail.entity.Drink;
import de.hsos.swa.mocktail.entity.DrinkKatalog;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class CockatilDbRepository implements DrinkKatalog {

    private static final Logger LOG = Logger.getLogger(CockatilDbRepository.class);


    @Inject
    @RestClient
    CocktailDbClient client;

    @Override
    @Timeout(3000)
    @Retry(maxRetries = 2, delay = 500)
    @CircuitBreaker(requestVolumeThreshold = 4, failureRatio = 0.5,delay = 10000)
    @Fallback(fallbackMethod = "keineDrinks")
    public List<Drink> sucheNachName(String name) {
        LOG.debugf("CocktailDB: suche name=%s", name);
        return uebersetze(client.sucheNachName(name));

    }

    @Override
    @Timeout(3000)
    @Retry(maxRetries = 2, delay = 500)
    @CircuitBreaker(requestVolumeThreshold = 4, failureRatio = 0.5,delay = 10000)
    @Fallback(fallbackMethod = "keineDrinks")
    public List<Drink> sucheNachKategorie(String  alkohol) {
        LOG.debugf("CocktailDB: filter alkoholfrei e=%s", alkohol);
        return uebersetze(client.filterNachAlkohol(alkohol));
        }


    @Override
    @Timeout(3000)
    @Retry(maxRetries = 2, delay = 500)
    @CircuitBreaker(requestVolumeThreshold = 4, failureRatio = 0.5,delay = 10000)
    @Fallback(fallbackMethod = "keineDrinks")
    public List<Drink> sucheNachZutat(String zutat) {
        LOG.debugf("CocktailDB: suche Zutat=%s",zutat);
        return uebersetze(client.sucheNachZutat(zutat));
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
    List<Drink> keineDrinks(String suchtext) {
        LOG.warnf("CocktailDB nicht erreichbar oder Antwort unlesbar, Fallback fuer '%s'", suchtext);
        return new ArrayList<>();
    }
}
