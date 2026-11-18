package de.hsos.swa.mocktail.boundary.rest;


import de.hsos.swa.mocktail.boundary.dto.MocktailDTO;
import de.hsos.swa.mocktail.boundary.dto.MocktailEingabeDTO;
import de.hsos.swa.mocktail.entity.Mocktail;
import de.hsos.swa.mocktail.entity.MocktailKatalog;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.jboss.logging.Logger;

import java.net.URI;
import java.util.List;

@Path("/drinks/mocktail")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MocktailResource {

    private static final Logger LOG = Logger.getLogger(MocktailResource.class.getName());

    @Inject
    MocktailKatalog katalog;

    @GET
    @Operation(summary = "Mocktails auflisten oder suchen",description = "Liefert alle Mocktails. Mit name und/oder zutat wird gefiltert")
    @APIResponses({
            @APIResponse(responseCode = "200",description = "Liste der gefundenen Mocktails",
            content = @Content(schema = @Schema(type = SchemaType.ARRAY,implementation = MocktailDTO.class)))
    })
    public List<MocktailDTO> alleMocktails(
            @Parameter(description = "Teil des Namens, Gross-/Kleinschreibung egal") @QueryParam("name") String name,
            @Parameter(description = "Teil eines Zutatennamens") @QueryParam("zutat") String zutat){
        LOG.debugf("GET /drinks/mocktail name=%s zutat=%s", name, zutat);
        List<Mocktail> mocktails;
        if(name == null && zutat == null){
            mocktails = katalog.alle();
        } else {
            mocktails = katalog.finde(name, zutat);
        }
        LOG.debugf("%d Mocktails gefunden", mocktails.size());
        return mocktails.stream().map(MocktailDTO::von).toList();

    }
    @POST
    @Operation(summary = "Mocktail anlegen",description = "Legt einen neuen Mocktail an und vergibt die ID")
        @APIResponses({
                @APIResponse(responseCode = "201", description = "Mocktail angelegt",content = @Content(schema = @Schema(implementation = MocktailDTO.class))),
                @APIResponse(responseCode = "400",description = "Ungueltige Eingabe")
        })
    public Response erstelleMocktail(MocktailEingabeDTO dto, @Context UriInfo uriInfo) {
        LOG.debug("POST /drinks/mocktail");
        if(dto == null){
            throw new IllegalArgumentException("Request fehlt");
        }
        Mocktail neuerMocktail = katalog.speichere(new Mocktail(dto.name,dto.beschreibung,dto.zubereitung,dto.zutatenAlsEntity()));
        URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(neuerMocktail.getId())).build();
        LOG.infof("Mocktail %d '%s' erstellt",neuerMocktail.getId(), neuerMocktail.getName());
        return Response.created(location).entity(MocktailDTO.von(neuerMocktail)).build();
    }
}
