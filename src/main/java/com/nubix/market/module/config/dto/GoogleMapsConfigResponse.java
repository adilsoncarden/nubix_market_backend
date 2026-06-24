package com.nubix.market.module.config.dto;

/**
 * DTO de respuesta con la clave de API de Google Maps.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
public class GoogleMapsConfigResponse {

    /** Clave de API de Google Maps. */
    private String apiKey;
    /** Indica si la API está configurada. */
    private boolean configured;

    public GoogleMapsConfigResponse() {
    }

    public GoogleMapsConfigResponse(String apiKey, boolean configured) {
        this.apiKey = apiKey;
        this.configured = configured;
    }

    /**
     * GetApiKey.
     * @return resultado de la operación
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * SetApiKey.
     * @param apiKey Clave de API de Google Maps.
     */
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * IsConfigured.
     * @return resultado de la operación
     */
    public boolean isConfigured() {
        return configured;
    }

    /**
     * SetConfigured.
     * @param configured Indica si la API está configurada.
     */
    public void setConfigured(boolean configured) {
        this.configured = configured;
    }
}
