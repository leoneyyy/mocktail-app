package de.hsos.swa.mocktail.entity;

public class MocktailNichtGefundenException extends RuntimeException {
    public MocktailNichtGefundenException(long id) {
        super("Mocktail mit ID " + id + " nicht gefunden");
    }
}
