package com.nubix.market.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Configuración del cliente HTTP {@link RestTemplate} para integraciones salientes
 * del backend de Nubix Market (pagos, mapas, servicios externos) con timeouts definidos.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@Configuration
public class RestTemplateConfig {

    /**
     * Expone un {@link RestTemplate} con tiempos de conexión y lectura acotados.
     *
     * @return cliente REST configurado para llamadas HTTP salientes
     */
    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(8_000);
        factory.setReadTimeout(12_000);
        return new RestTemplate(factory);
    }
}
