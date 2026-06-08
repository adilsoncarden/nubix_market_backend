package com.nubix.market.module.config.dto;

public class GoogleMapsConfigResponse {

    private String apiKey;
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
