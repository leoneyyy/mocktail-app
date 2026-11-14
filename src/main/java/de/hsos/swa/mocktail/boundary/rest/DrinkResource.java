package de.hsos.swa.mocktail.boundary.rest;


import de.hsos.swa.mocktail.boundary.dto.DrinkDTO;
import de.hsos.swa.mocktail.entity.Drink;
import de.hsos.swa.mocktail.entity.DrinkKatalog;
import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.jboss.logging.Logger;

import java.util.List;

@Path("/drinks")
@Produces(MediaType.APPLICATION_JSON)
public class DrinkResource {
    private static final Logger LOG = Logger.getLogger(DrinkResource.class);

    @Inject
    DrinkKatalog katalog;

    @GET
    @Operation(summary = "Drink nach Namen suchen", description = "Suche bei TheCocktailDB nach Drinks, deren Name den Suchtext enthaelt")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Gefundene Drinks"),
            @APIResponse(responseCode = "404", description = "Kein Drink mit diesem Namen gefunden")
    })
    @Path("name/{name}")
    public List<DrinkDTO> nachName(@Parameter(description = "Name des Getraenks") @PathParam("name") String name) {
        LOG.debugf("GET /drinks/name/%s", name);
        return katalog.sucheNachName(name).stream().map(DrinkDTO::von).toList();
    }
    @GET
    @Path("zutat/{zutat}")
    @Operation(summary = "Drinks nach Zutat suchen",
    description = "Liefert Drinks mit dieser Zutat")
    @APIResponses({
            @APIResponse(responseCode = "200",description = "Gefundene Drinks"),
            @APIResponse(responseCode = "404",description = "Kein Drink mit dieer Zutat gefunden")
    })
    public List<DrinkDTO> nachZutat(@PathParam("zutat") String zutat) {
        LOG.debugf("GET /drinks/zutat/%s", zutat);
        return katalog.sucheNachZutat(zutat).stream().map(DrinkDTO::von).toList();
    }
    @GET
    @Operation(summary = "Drinks nach Kategorie suchen",
            description = "Kategorie ist mocktail (alkoholfrei) oder cocktail (alkoholisch)")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Gefundene Drinks"),
            @APIResponse(responseCode = "400", description = "Unbekannte Kategorie"),
            @APIResponse(responseCode = "404", description = "Keine Drinks gefunden")
    })
    @Path("kategorie/{kategorie}")
    public List<DrinkDTO> nachKategorie(@PathParam("kategorie") String kategorie) {
        LOG.debugf("GET /drinks/kategorie/%s", kategorie);
        String alkohol;
        if(kategorie.equalsIgnoreCase("mocktail")){
            alkohol = "Non_Alcoholic";

        }else if(kategorie.equalsIgnoreCase("cocktail")){
            alkohol = "Alcoholic";
        }else {
            throw new IllegalArgumentException("Kategorie muss mocktail oder cocktail sein");
        }
        return alsDto(katalog.sucheNachKategorie(alkohol), "Kategorie " + kategorie);
    }

    private List<DrinkDTO> alsDto(List<Drink> drinks, String suche) {
        if (drinks.isEmpty()) {
            throw new NotFoundException("Keine Drinks gefunden fuer " + suche);
        }
        return drinks.stream().map(DrinkDTO::von).toList();
    }




}
