package com.nubix.market.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Clase de configuración de Spring encargada de proveer el cliente HTTP para realizar
 * peticiones hacia servicios o APIs externas.
 */
@Configuration
public class RestTemplateConfig {

    /**
     * Crea y expone un bean de {@link RestTemplate} con configuraciones de tiempo de espera (timeout)
     * personalizadas para evitar que los hilos de la aplicación se bloqueen indefinidamente
     * si el servicio externo no responde.
     *
     * @return Una instancia de RestTemplate configurada con un timeout de conexión de 8 segundos 
     * y un timeout de lectura de 12 segundos.
     */
    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(8_000);
        factory.setReadTimeout(12_000);
        return new RestTemplate(factory);
    }
}
