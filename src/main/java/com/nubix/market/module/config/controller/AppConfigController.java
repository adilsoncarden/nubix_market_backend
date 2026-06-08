package com.nubix.market.module.config.controller;

import com.nubix.market.module.config.dto.GoogleMapsConfigResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/config")
public class AppConfigController {

    private static final String PLACEHOLDER_KEY = "TU_GOOGLE_MAPS_API_KEY_AQUI";

    @Value("${google.maps.api-key:}")
    private String googleMapsApiKey;

    @GetMapping("/google-maps-key")
    public ResponseEntity<GoogleMapsConfigResponse> obtenerGoogleMapsApiKey() {
        boolean configured = StringUtils.isNotBlank(googleMapsApiKey)
                && !PLACEHOLDER_KEY.equals(googleMapsApiKey.trim());
        String key = configured ? googleMapsApiKey.trim() : "";
        return ResponseEntity.ok(new GoogleMapsConfigResponse(key, configured));
    }
}
