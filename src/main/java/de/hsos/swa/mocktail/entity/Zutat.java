package de.hsos.swa.mocktail.entity;

public record Zutat(String name, double menge, String einheit) {

    public Zutat{

        if(name == null || name.isBlank()){
            throw new IllegalArgumentException("Zutat muss ein Namen haben");
        }
        if(menge < 0){
            throw new IllegalArgumentException("Menge darf nicht negativ sein");
        }
        if(einheit == null || einheit.isBlank()){
            einheit="";
        }
    }
}
