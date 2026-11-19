package de.hsos.swa.cocktail.gateway.cocktaildb;


import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "cocktaildb")
@Produces(MediaType.APPLICATION_JSON)
public interface CocktailDbClient {

    @GET
    @Path("/search.php")
    CocktailDbAntwort sucheNachName(@QueryParam("s") String name);

    @GET
    @Path("/filter.php")
    CocktailDbAntwort sucheNachZutat(@QueryParam("i") String Zutat);
    @GET
    @Path("/filter.php")
    CocktailDbAntwort filterNachAlkohol(@QueryParam("a") String alkohol);
    @GET
    @Path("/lookup.php")
    CocktailDbAntwort finde(@QueryParam("i") String id);


}
