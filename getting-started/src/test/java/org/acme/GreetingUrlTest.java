package org.acme;

import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
@QuarkusTest
public class GreetingUrlTest {

    @TestHTTPResource("/hello")
    URL testUrl;

    @Test
    void helloPerUrl() throws IOException {
        try (InputStream in = testUrl.openStream()) {
            String antwort = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            assertEquals("Hello from Quarkus REST", antwort);
        }
    }
}
