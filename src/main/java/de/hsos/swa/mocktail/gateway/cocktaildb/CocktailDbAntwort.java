package de.hsos.swa.mocktail.gateway.cocktaildb;

public class CocktailDbAntwort {

    /**
     * TheCocktailDB liefert hier je nach Endpunkt und Treffer eine Liste von Drinks,
     * null (search.php ohne Treffer) oder den String "no data found" (filter.php ohne Treffer).
     * Deshalb Object statt List, sonst schlaegt das Deserialisieren fehl.
     */
    public Object drinks;
}
