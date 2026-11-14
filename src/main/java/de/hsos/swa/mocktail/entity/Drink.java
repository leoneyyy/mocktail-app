package de.hsos.swa.mocktail.entity;

import java.util.List;

public record Drink(String id, String name, String zubereitung, List<String> zutaten) {

}
