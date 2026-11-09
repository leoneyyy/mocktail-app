package de.hsos.swa.mocktail.entity;

public class MocktailNichtGefundenExeption extends RuntimeException {
    public MocktailNichtGefundenExeption(String message) {
        super(message);
    }
}
