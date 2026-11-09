package de.hsos.swa.mocktail.entity;

import java.util.List;

public interface MocktailKatalog {

    List<Mocktail> alle();

    Mocktail finde(long id);

    Mocktail speichere(Mocktail mocktail);

    boolean loesche(long id);

}
