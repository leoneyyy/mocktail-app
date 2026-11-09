package de.hsos.swa.mocktail.boundary.rest;

import de.hsos.swa.mocktail.entity.MocktailNichtGefundenException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Map;
@Provider
public class FehlerMapper implements ExceptionMapper<MocktailNichtGefundenException> {
    @Override
    public Response toResponse(MocktailNichtGefundenException exception) {
        return Response.status(Response.Status.NOT_FOUND).entity(Map.of("Fehler", exception.getMessage())).build();
    }
}
