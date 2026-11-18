package de.hsos.swa.mocktail.boundary.rest;

import de.hsos.swa.mocktail.entity.MocktailNichtGefundenException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

import java.util.Map;
@Provider
public class FehlerMapper implements ExceptionMapper<MocktailNichtGefundenException> {

    private static final Logger LOG = Logger.getLogger(FehlerMapper.class.getName());

    @Override
    public Response toResponse(MocktailNichtGefundenException exception) {
            LOG.warnf("404 Not found: %s",exception.getMessage());
        return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(Map.of("fehler", exception.getMessage())).build();
    }
}
