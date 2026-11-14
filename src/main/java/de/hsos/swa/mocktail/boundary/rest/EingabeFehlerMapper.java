package de.hsos.swa.mocktail.boundary.rest;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

import java.util.Map;
@Provider
public class EingabeFehlerMapper implements ExceptionMapper<IllegalArgumentException> {

    private static final Logger LOG = Logger.getLogger(EingabeFehlerMapper.class.getName());

    @Override
    public Response toResponse(IllegalArgumentException e) {
        LOG.warnf("400 bad Request: %s", e.getMessage());
        return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(Map.of("fehler",e.getMessage())).build();
    }
}
