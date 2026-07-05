package com.nubix.market.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Configuración central de Jackson para serialización y deserialización JSON
 * en la API REST de Nubix Market (fechas ISO-8601 y tolerancia a propiedades desconocidas).
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@Configuration
public class JacksonConfig {

    /**
     * Expone el {@link ObjectMapper} principal de la aplicación con módulos JSR-310
     * registrados y políticas de serialización alineadas al contrato de la API.
     *
     * @return instancia configurada de {@link ObjectMapper}
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
