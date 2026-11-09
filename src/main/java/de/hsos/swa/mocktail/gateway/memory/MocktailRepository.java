package de.hsos.swa.mocktail.gateway.memory;


import de.hsos.swa.mocktail.entity.Mocktail;
import de.hsos.swa.mocktail.entity.MocktailKatalog;
import de.hsos.swa.mocktail.entity.Zutat;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@ApplicationScoped
public class MocktailRepository implements MocktailKatalog {


    private final Map<Long, Mocktail> mocktails = new ConcurrentHashMap<>();

    private final AtomicLong idGenerator = new AtomicLong(1);


    @PostConstruct
    void beispieldaten() {
        speichere(new Mocktail("Virgin Mojito", "Frisch mit Minze und Limette",
                "Minze und Limette zerdrücken, Eis dazu, mit Soda auffüllen",
                List.of(new Zutat("Limettensaft", 20, "ml"),
                        new Zutat("Minze", 8, "Blätter"),
                        new Zutat("Soda", 150, "ml"))));
        speichere(new Mocktail("Virgin Colada", "Cremig mit Ananas und Kokos",
                "Alles mit Eis im Mixer pürieren",
                List.of(new Zutat("Ananassaft", 120, "ml"),
                        new Zutat("Kokosmilch", 40, "ml"))));
    }

    @Override
    public List<Mocktail> alle() {
        return new ArrayList<>(mocktails.values());
    }

    @Override
    public Mocktail finde(long id) {
        return mocktails.get(id);
    }

    @Override
    public Mocktail speichere(Mocktail mocktail) {
        if(mocktail.getId() ==0){
            mocktail.setId(this.idGenerator.getAndIncrement());
        }

        mocktails.put(mocktail.getId(), mocktail);
        return mocktail;
    }

    @Override
    public boolean loesche(long id) {
        return mocktails.remove(id) != null;
    }






}
