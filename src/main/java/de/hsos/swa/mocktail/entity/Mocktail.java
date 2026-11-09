package de.hsos.swa.mocktail.entity;

import de.hsos.swa.mocktail.boundary.dto.ZutatDTO;

import java.util.*;

public class Mocktail {

    private long id;
    private String name;
    private String beschreibung;
    private String zubereitung;
    private final List<Zutat> zutaten = new ArrayList<Zutat>();


    public Mocktail(String name, String beschreibung, String zubereitung, List<Zutat> zutaten) {
        aendere(name,beschreibung,zubereitung,zutaten);
    }

    public void aendere(String name, String beschreibung, String zubereitung, List<Zutat> zutaten) {
        if(name==null|| name.isBlank()){
            throw new IllegalArgumentException("Name darf nich leer sein");
        }
        if(zutaten==null || zutaten.isEmpty()){
            throw new IllegalArgumentException("Ein Mocktail muss mindestens eine Zutat haben");
        }
        this.name=name.trim();
        this.beschreibung=beschreibung;
        this.zubereitung=zubereitung;
        this.zutaten.clear();
        this.zutaten.addAll(zutaten);
    }

    public boolean enthaeltZutat(String zutat){
        String name = zutat.toLowerCase();
        return zutaten.stream().anyMatch(z -> z.name().toLowerCase().contains(name));
    }

    public long getId() {
        return id;
    }



    public String getName() {
        return name;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public String getZubereitung() {
        return zubereitung;
    }

    public List<Zutat> getZutaten() {
        return Collections.unmodifiableList(zutaten);
    }

    public void setId(long id) {
        if(this.id !=0){
            throw  new IllegalStateException("id ist schon vergeben");
        }
        this.id = id;

    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Mocktail andere && id != 0 && andere.id == id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
