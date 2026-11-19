package de.hsos.swa.cocktail.boundary.rest;

import de.hsos.swa.cocktail.boundary.dto.CocktailDTO;
import de.hsos.swa.cocktail.entity.Drink;
import de.hsos.swa.cocktail.entity.DrinkKatalog;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;

import java.util.List;

@Tag(name = "Cocktails")
@Path("/drinks/cocktail/{id}")
@Produces(MediaType.APPLICATION_JSON)
public class CocktailIdResource {

    private static final Logger LOG = Logger.getLogger(CocktailIdResource.class);

    @Inject
    DrinkKatalog katalog;

    @GET
    @Operation(summary = "Cocktail finden", description = "Cocktail von TheCocktailDB anhand der ID, mit Zubereitung und Zutaten")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Cocktail gefunden",
                    content = @Content(schema = @Schema(implementation = CocktailDTO.class))),
            @APIResponse(responseCode = "404", description = "Cocktail nicht gefunden")
    })
    public CocktailDTO cocktail(@Parameter(description = "Die ID eines Cocktails", example = "17835") @PathParam("id") String id) {
        LOG.debugf("GET /drinks/cocktail/%s", id);
        List<Drink> treffer = katalog.finde(id);
        if (treffer.isEmpty()) {
            throw new NotFoundException("Cocktail mit ID " + id + " nicht gefunden");
        }
        return CocktailDTO.von(treffer.get(0));
    }
}
