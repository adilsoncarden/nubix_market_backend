package com.nubix.market.module.config.dto;

/**
 * Objeto de Transferencia de Datos (DTO) que encapsula la respuesta del servidor 
 * al solicitar la configuración del servicio de Google Maps.
 */
public class GoogleMapsConfigResponse {

    /** La clave alfanumérica (API Key) necesaria para inicializar el mapa en el frontend. */
    private String apiKey;

    /** Bandera que indica si la clave proporcionada es válida y el servicio está listo para usarse. */
    private boolean configured;

    public GoogleMapsConfigResponse() {
    }

    public GoogleMapsConfigResponse(String apiKey, boolean configured) {
        this.apiKey = apiKey;
        this.configured = configured;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public boolean isConfigured() {
        return configured;
    }

    public void setConfigured(boolean configured) {
        this.configured = configured;
    }
}
