package de.hsos.swa.mocktail.boundary.rest;


import de.hsos.swa.mocktail.boundary.dto.MocktailDTO;
import de.hsos.swa.mocktail.boundary.dto.MocktailEingabeDTO;
import de.hsos.swa.mocktail.entity.Mocktail;
import de.hsos.swa.mocktail.entity.MocktailKatalog;
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
    MocktailKatalog katalog;

    @GET
    public List<MocktailDTO> alleMocktails(@QueryParam("name") String name,@QueryParam("zutat") String zutat){
        List<Mocktail> mocktails;
        if(name == null && zutat == null){
            mocktails = katalog.alle();
        }else {
            mocktails = katalog.finde(name,zutat);
        }
        return mocktails.stream().map(MocktailDTO::von).toList();

    }
    @POST
    public Response erstelleMocktail(MocktailEingabeDTO dto, @Context UriInfo uriInfo) {
        if(dto == null){
            throw new IllegalArgumentException("Request fehlt");
        }
        Mocktail neuerMocktail = katalog.speichere(new Mocktail(dto.name,dto.beschreibung,dto.zubereitung,dto.zutatenAlsEntity()));
        URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(neuerMocktail.getId())).build();
        return Response.created(location).entity(MocktailDTO.von(neuerMocktail)).build();
    }
}
