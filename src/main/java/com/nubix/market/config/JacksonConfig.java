package com.nubix.market.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Clase de configuración de Spring para personalizar la serialización y deserialización de JSON.
 * Configura globalmente el comportamiento de Jackson (ObjectMapper) en toda la aplicación.
 */
@Configuration
public class JacksonConfig {

    /**
     * Crea y expone un bean principal (Primary) de {@link ObjectMapper} con configuraciones personalizadas.
     * <p>
     * Configuraciones aplicadas:
     * - Registra automáticamente módulos adicionales (ej. soporte para fechas de Java 8).
     * - Deshabilita la escritura de fechas como timestamps (para usar formato ISO-8601).
     * - Evita que la aplicación lance errores si el JSON entrante contiene propiedades desconocidas.
     * </p>
     *
     * @return Una instancia configurada de ObjectMapper lista para ser inyectada por Spring.
     */
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .findAndRegisterModules()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }
}
