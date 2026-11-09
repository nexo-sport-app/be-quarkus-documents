package it.nexoapp.doc;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.quarkus.test.junit.QuarkusTest;
import it.nexoapp.doc.config.DocumentsConfig;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.ConfigProvider;
import org.junit.jupiter.api.Test;

/**
 * Boots the documents service alone (Postgres, RabbitMQ and Keycloak from Dev Services, no other Nexo service) and
 * checks that it reports healthy without a token and that its service-owned configuration resolves its defaults.
 */
@QuarkusTest
class HealthTest {

    /** Service-owned configuration mapping (prefix {@code doc}). */
    @Inject
    DocumentsConfig config;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The health endpoint is public and reports UP. It lives at the absolute {@code /q/health}, outside the service
     * root path, so the rest-assured base path (the root path) is reset.
     */
    @Test
    void healthIsUpWithoutToken() {
        given().basePath("/").when().get("/q/health").then().statusCode(200).body("status", equalTo("UP"));
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The default exchange is {@code nexo.doc} and the outbox channel resolves to it.
     */
    @Test
    void messagingExchangeDefaultsToServiceExchange() {
        assertEquals("nexo.doc", config.messaging().exchange());
        assertEquals("nexo.doc", ConfigProvider.getConfig()
                .getValue("mp.messaging.outgoing.nexo-outbox.exchange.name", String.class));
    }

}
