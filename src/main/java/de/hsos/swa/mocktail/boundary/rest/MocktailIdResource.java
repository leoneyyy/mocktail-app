package de.hsos.swa.mocktail.boundary.rest;


import de.hsos.swa.mocktail.boundary.dto.MocktailDTO;
import de.hsos.swa.mocktail.boundary.dto.MocktailEingabeDTO;
import de.hsos.swa.mocktail.entity.Mocktail;
import de.hsos.swa.mocktail.entity.MocktailKatalog;
import de.hsos.swa.mocktail.entity.MocktailNichtGefundenException;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;


@Path("/mocktails/{id}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MocktailIdResource {

    private static final Logger LOG = Logger.getLogger(MocktailIdResource.class.getName());
    @Inject
    MocktailKatalog katalog;
    @PUT
    public MocktailDTO aendereMocktail(@PathParam("id") long id, MocktailEingabeDTO dto){
        LOG.debugf("PUT /mocktails/%d", id);
        if(dto == null){
            throw new IllegalArgumentException("Request fehlt");
        }
        Mocktail vorhandener = katalog.finde(id);
        if(vorhandener == null){
            throw new MocktailNichtGefundenException(id);
        }
        vorhandener.aendere(dto.name, dto.beschreibung, dto.zubereitung, dto.zutatenAlsEntity());
        LOG.infof("Mocktail %d '%s' geaendert",vorhandener.getId(),vorhandener.getName());
        return MocktailDTO.von(katalog.speichere(vorhandener));
    }

    @DELETE
    public Response loescheMocktail(@PathParam("id") long id){
       LOG.debugf("DELETE /mocktails/%d", id);
        if(!katalog.loesche(id)){
            throw new MocktailNichtGefundenException(id);
        }
        LOG.infof("Mocktail %d gelöscht", id);
        return Response.noContent().build();
    }

    @GET
    public MocktailDTO mocktail(@PathParam("id") long id){
        LOG.debugf("GET /mocktails/%d", id);
        Mocktail m = katalog.finde(id);
        if(m == null){
            throw new MocktailNichtGefundenException(id);
        }
        LOG.debugf("Mocltail gefunden %s",m.getName());
        return MocktailDTO.von(m);
    }


}
