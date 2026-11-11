package de.hsos.swa.mocktail.boundary.rest;


import de.hsos.swa.mocktail.boundary.dto.MocktailDTO;
import de.hsos.swa.mocktail.boundary.dto.MocktailEingabeDTO;
import de.hsos.swa.mocktail.control.MocktailAbfragen;
import de.hsos.swa.mocktail.control.MocktailAendern;
import de.hsos.swa.mocktail.control.MocktailErstellen;
import de.hsos.swa.mocktail.control.MocktailLoeschen;
import de.hsos.swa.mocktail.entity.Mocktail;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.List;

@Path("/mocktails")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MocktailResource {



    @Inject
    MocktailAbfragen abfragen;

    @Inject
    MocktailErstellen erstellen;

    @Inject
    MocktailAendern aendern;

    @Inject
    MocktailLoeschen loeschen;

    @GET
    public List<MocktailDTO> alleMocktails(@QueryParam("name") String name,@QueryParam("zutat") String zutat){
        List<Mocktail> mocktails;
        if(name == null && zutat == null){
            mocktails = abfragen.alle();
        }else {
            mocktails=abfragen.suche(name, zutat);
        }
        return mocktails.stream().map(MocktailDTO::von).toList();

    }
    @POST
    public Response erstelleMocktail(MocktailEingabeDTO dto, @Context UriInfo uriInfo) {
        if(dto == null){
            throw new IllegalArgumentException("Request fehlt");
        }
        Mocktail neuerMocktail = erstellen.erstelle(dto.name,dto.beschreibung,dto.zubereitung,dto.zutatenAlsEntity());
        URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(neuerMocktail.getId())).build();
        return Response.created(location).entity(MocktailDTO.von(neuerMocktail)).build();
    }

    @PUT
    @Path("{id}")
    public MocktailDTO aendereMocktail(@PathParam("id") long id, MocktailEingabeDTO dto){
        if(dto == null){
            throw new IllegalArgumentException("Request fehlt");
        }
        Mocktail geaendert = aendern.aendere(id, dto.name, dto.beschreibung, dto.zubereitung, dto.zutatenAlsEntity());
        return MocktailDTO.von(geaendert);
    }

    @DELETE
    @Path("{id}")
    public Response loescheMocktail(@PathParam("id") long id){
        loeschen.loesche(id);
        return Response.noContent().build();
    }

    @GET
    @Path("{id}")
    public MocktailDTO mocktail(@PathParam("id") long id){
        return MocktailDTO.von(abfragen.finde(id));
    }


}
