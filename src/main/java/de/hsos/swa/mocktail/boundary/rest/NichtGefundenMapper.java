package de.hsos.swa.mocktail.boundary.rest;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

import java.util.Map;

/**
 * Liefert auch fuer NotFoundException (unbekannter Cocktail, unbekannter Pfad)
 * dasselbe JSON-Fehlerformat wie die anderen Mapper.
 */
@Provider
public class NichtGefundenMapper implements ExceptionMapper<NotFoundException> {

    private static final Logger LOG = Logger.getLogger(NichtGefundenMapper.class);

    @Override
    public Response toResponse(NotFoundException e) {
        String meldung = e.getMessage() != null ? e.getMessage() : "Nicht gefunden";
        LOG.warnf("404 Not found: %s", meldung);
        return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON)
                .entity(Map.of("fehler", meldung)).build();
    }
}
