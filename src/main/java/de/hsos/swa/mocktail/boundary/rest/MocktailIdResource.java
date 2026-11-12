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

@Path("/mocktails/{id}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MocktailIdResource {

    @Inject
    MocktailKatalog katalog;

    @PUT
    public MocktailDTO aendereMocktail(@PathParam("id") long id, MocktailEingabeDTO dto){
        if(dto == null){
            throw new IllegalArgumentException("Request fehlt");
        }
        Mocktail vorhandener = katalog.finde(id);
        if(vorhandener == null){
            throw new MocktailNichtGefundenException(id);
        }
        vorhandener.aendere(dto.name, dto.beschreibung, dto.zubereitung, dto.zutatenAlsEntity());
        return MocktailDTO.von(katalog.speichere(vorhandener));
    }

    @DELETE
    public Response loescheMocktail(@PathParam("id") long id){
        if(!katalog.loesche(id)){
            throw new MocktailNichtGefundenException(id);
        }
        return Response.noContent().build();
    }

    @GET
    public MocktailDTO mocktail(@PathParam("id") long id){
        Mocktail m = katalog.finde(id);
        if(m == null){
            throw new MocktailNichtGefundenException(id);
        }

        return MocktailDTO.von(m);
    }


}
