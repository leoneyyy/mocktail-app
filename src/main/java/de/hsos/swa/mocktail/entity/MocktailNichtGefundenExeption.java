package de.hsos.swa.mocktail.entity;

public class MocktailNichtGefundenExeption extends RuntimeException {
    public MocktailNichtGefundenExeption(long id) {
        super("Mocktail mit ID " + id + " nicht gefunden");
    }
}
