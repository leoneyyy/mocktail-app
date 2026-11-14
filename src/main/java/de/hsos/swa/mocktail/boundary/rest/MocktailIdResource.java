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
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.jboss.logging.Logger;


@Path("/mocktails/{id}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MocktailIdResource {

    private static final Logger LOG = Logger.getLogger(MocktailIdResource.class.getName());
    @Inject
    MocktailKatalog katalog;
    @PUT
    @Operation(summary = "Aenderung des Mocktail-Rezepts", description = "Aendere die Inhalte des Mocktail-Rezepts")
    @APIResponses({
            @APIResponse(responseCode = "200",description = "Mocktail-Rezept erfolgreich geändert",
            content = @Content(schema = @Schema(implementation = MocktailDTO.class))),
            @APIResponse(responseCode = "400",description = "Ungueltige Eingabe"),
            @APIResponse(responseCode = "404",description = "Mocktail-Rezept nicht gefunden")
    })
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

        Mocktail gespeichert = katalog.speichere(vorhandener);
        LOG.infof("Mocktail %d '%s' geaendert",gespeichert.getId(),gespeichert.getName());
        return MocktailDTO.von(gespeichert);
    }

    @DELETE
    @Operation(summary = "Mocktail löschen",description = "Mocktail-Rezept löschen")
    @APIResponses({
            @APIResponse(responseCode = "204",description = "Mocktail-Rezept erfolgreich gelöscht"),
            @APIResponse(responseCode = "404", description = "Mocktail-Rezept nicht gefunden")
    })
    public Response loescheMocktail(@PathParam("id") long id){
       LOG.debugf("DELETE /mocktails/%d", id);
        if(!katalog.loesche(id)){
            throw new MocktailNichtGefundenException(id);
        }
        LOG.infof("Mocktail %d gelöscht", id);
        return Response.noContent().build();
    }

    @GET
    @Operation(summary = "Mocktail finden",description = "Mocktail-Rezept anhand der ID finden")
    @APIResponses({
            @APIResponse(responseCode = "200",description = "Mocktail-Rezept gefunden",
            content = @Content(schema = @Schema(implementation = MocktailDTO.class))),
            @APIResponse(responseCode = "404", description = "Mocktail-Rezept nicht gefunden")
    })
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
