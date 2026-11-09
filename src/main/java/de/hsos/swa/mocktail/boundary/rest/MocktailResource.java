package de.hsos.swa.mocktail.boundary.rest;


import de.hsos.swa.mocktail.boundary.dto.MocktailDTO;
import de.hsos.swa.mocktail.control.MocktailAbfragen;
import de.hsos.swa.mocktail.control.MocktailAendern;
import de.hsos.swa.mocktail.control.MocktailErstellen;
import de.hsos.swa.mocktail.control.MocktailLoeschen;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/mocktails")
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



}
