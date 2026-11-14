package de.hsos.swa.mocktail.entity;

import java.util.*;

public class Mocktail {

    private final long id;
    private String name;
    private String beschreibung;
    private String zubereitung;
    private final List<Zutat> zutaten = new ArrayList<Zutat>();


    public Mocktail(String name, String beschreibung, String zubereitung, List<Zutat> zutaten) {
        this(0,name,beschreibung,zubereitung,zutaten);
    }
    public Mocktail(long id, String name, String beschreibung,String zubereitung, List<Zutat> zutaten) {
        this.id = id;
        aendere(name,beschreibung,zubereitung,zutaten);
    }

    public final void aendere(String name, String beschreibung, String zubereitung, List<Zutat> zutaten) {
        if(name==null|| name.isBlank()){
            throw new IllegalArgumentException("Name darf nicht leer sein");
        }
        if(zutaten==null || zutaten.isEmpty()){
            throw new IllegalArgumentException("Ein Mocktail muss mindestens eine Zutat haben");
        }
        this.name=name.trim();
        this.beschreibung=beschreibung;
        this.zubereitung=zubereitung;
        List<Zutat> kopieZutaten = List.copyOf(zutaten);
        this.zutaten.clear();
        this.zutaten.addAll(kopieZutaten);
    }

    public boolean enthaeltZutat(String zutat){
        for (Zutat z : zutaten) {
            if (z.name().toLowerCase().contains(zutat.toLowerCase())) {
                return true;
            }
        }
        return false;
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

    @Override
    public boolean equals(Object o) {
        return o instanceof Mocktail andere && id != 0 && andere.id == id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
