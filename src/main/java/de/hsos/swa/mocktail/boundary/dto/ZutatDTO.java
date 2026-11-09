package de.hsos.swa.mocktail.boundary.dto;

import de.hsos.swa.mocktail.entity.Zutat;

public class ZutatDTO {

    public String name;
    public double menge;
    public String einheit;

    public ZutatDTO() {}

    public ZutatDTO(String name, double menge, String einheit) {
        this.name = name;
        this.menge = menge;
        this.einheit = einheit;
    }

    public static ZutatDTO von(Zutat zutat){
        return new ZutatDTO(zutat.name(),zutat.menge(),zutat.einheit());
    }

    public Zutat zuZutat(){
        return new Zutat(name,menge,einheit);
    }

}
