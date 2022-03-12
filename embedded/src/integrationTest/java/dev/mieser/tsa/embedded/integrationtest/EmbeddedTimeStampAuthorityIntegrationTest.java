package dev.mieser.tsa.embedded.integrationtest;

import org.junit.jupiter.api.Test;

import dev.mieser.tsa.embedded.EmbeddedHttpTimeStampAuthority;

class EmbeddedTimeStampAuthorityIntegrationTest {

    @Test
    void lol() {
        try (var embeddedTsa = new EmbeddedHttpTimeStampAuthority()) {
            embeddedTsa.start();
        }
    }

}
