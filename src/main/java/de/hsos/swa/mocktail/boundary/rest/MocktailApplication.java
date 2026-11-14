package de.hsos.swa.mocktail.boundary.rest;

import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Mocktail-API",
                version = "1.0.0",
                description = "Verwaltung von Mocktail-Rezepten"
        )

)

public class MocktailApplication extends Application {
}
