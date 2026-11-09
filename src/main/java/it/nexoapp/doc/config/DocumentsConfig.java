package it.nexoapp.doc.config;

import io.quarkus.runtime.annotations.StaticInitSafe;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

/**
 * Documents microservice settings, mapped from configuration properties with the {@code doc} prefix.
 * <p>
 * Every property is optional and falls back to a default when not set.
 */
@StaticInitSafe
@ConfigMapping(prefix = "doc")
public interface DocumentsConfig {

    /**
     * Messaging settings (properties with the {@code doc.messaging} prefix).
     */
    Messaging messaging();

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Settings of the event messaging, mapped from the {@code doc.messaging} prefix.
     */
    interface Messaging {

        /**
         * Name of the topic exchange the outbox relay publishes this service's events on
         * (property {@code doc.messaging.exchange}). Optional, defaults to {@code nexo.doc}.
         */
        @WithDefault("nexo.doc")
        String exchange();

    }

}
