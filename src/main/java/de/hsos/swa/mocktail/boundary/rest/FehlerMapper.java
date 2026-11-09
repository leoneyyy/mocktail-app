package de.hsos.swa.mocktail.boundary.rest;

import de.hsos.swa.mocktail.entity.MocktailNichtGefundenExeption;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

import java.util.Map;

public class FehlerMapper implements ExceptionMapper<MocktailNichtGefundenExeption> {
    @Override
    public Response toResponse(MocktailNichtGefundenExeption exception) {
        return Response.status(Response.Status.NOT_FOUND).entity(Map.of("Fehler", exception.getMessage())).build();
    }
}
