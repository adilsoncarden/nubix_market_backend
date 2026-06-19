package com.nubix.market.module.config.controller;

import com.nubix.market.module.config.dto.GoogleMapsConfigResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST diseñado para proveer configuraciones dinámicas y variables de entorno 
 * desde el backend hacia el frontend.
 * Permite que la aplicación cliente (React) obtenga credenciales públicas de servicios de terceros
 * sin necesidad de tenerlas escritas directamente en su código fuente.
 */
@RestController
@RequestMapping("/api/config")
public class AppConfigController {

    /** Valor por defecto que indica que la clave aún no ha sido configurada. */
    private static final String PLACEHOLDER_KEY = "TU_GOOGLE_MAPS_API_KEY_AQUI";

    /** * Clave del API de Google Maps inyectada desde las variables de entorno o application.properties.
     * Si no existe, se inyecta una cadena vacía por defecto.
     */
    @Value("${google.maps.api-key:}")
    private String googleMapsApiKey;

    /**
     * Endpoint público para obtener la clave de configuración de Google Maps.
     * Evalúa si la clave está vacía o si sigue teniendo el valor de relleno por defecto 
     * para informarle al frontend si el servicio de mapas está realmente habilitado.
     *
     * @return Respuesta HTTP 200 (OK) con el DTO que contiene la clave y su estado de configuración.
     */
    @GetMapping("/google-maps-key")
    public ResponseEntity<GoogleMapsConfigResponse> obtenerGoogleMapsApiKey() {
        boolean configured = StringUtils.isNotBlank(googleMapsApiKey)
                && !PLACEHOLDER_KEY.equals(googleMapsApiKey.trim());
        String key = configured ? googleMapsApiKey.trim() : "";
        return ResponseEntity.ok(new GoogleMapsConfigResponse(key, configured));
    }
}
