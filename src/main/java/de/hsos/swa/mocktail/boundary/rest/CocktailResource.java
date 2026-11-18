package de.hsos.swa.mocktail.boundary.rest;

import de.hsos.swa.mocktail.boundary.dto.CocktailDTO;
import de.hsos.swa.mocktail.entity.Drink;
import de.hsos.swa.mocktail.entity.DrinkKatalog;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.jboss.logging.Logger;

import java.util.List;

@Path("/drinks/cocktail")
@Produces(MediaType.APPLICATION_JSON)
public class CocktailResource {

    private static final Logger LOG = Logger.getLogger(CocktailResource.class);

    @Inject
    DrinkKatalog katalog;

    @GET
    @Operation(summary = "Cocktails auflisten oder suchen",
            description = "Cocktails von TheCocktailDB. Ohne Filter alle alkoholischen Cocktails, "
                    + "mit name und/oder zutat wird gefiltert. Keine Treffer ergeben eine leere Liste.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Liste der gefundenen Cocktails",
                    content = @Content(schema = @Schema(type = SchemaType.ARRAY, implementation = CocktailDTO.class)))
    })
    public List<CocktailDTO> cocktails(
            @Parameter(description = "Teil des Namens", example = "mojito") @QueryParam("name") String name,
            @Parameter(description = "Zutat (englisch, wie bei TheCocktailDB)", example = "mint") @QueryParam("zutat") String zutat) {
        LOG.debugf("GET /drinks/cocktail name=%s zutat=%s", name, zutat);
        List<Drink> drinks;
        if (name != null) {
            // Die Namenssuche liefert die Zutaten mit, also hier lokal nach Zutat filtern
            drinks = katalog.sucheNachName(name);
            if (zutat != null) {
                drinks = drinks.stream().filter(d -> d.enthaeltZutat(zutat)).toList();
            }
        } else if (zutat != null) {
            drinks = katalog.sucheNachZutat(zutat);
        } else {
            drinks = katalog.sucheNachKategorie("Alcoholic");
        }
        LOG.debugf("%d Cocktails gefunden", drinks.size());
        return drinks.stream().map(CocktailDTO::von).toList();
    }
}
