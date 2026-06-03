package com.nubix.market.config;

import java.util.LinkedHashMap;
import java.util.Map;

public final class ApiErrorResponse {

    public static final String FORBIDDEN_MESSAGE =
            "No tiene los permisos necesarios para realizar esta acción.";

    private ApiErrorResponse() {
    }

    public static Map<String, Object> forbidden() {
        return body(403, FORBIDDEN_MESSAGE);
    }

    public static Map<String, Object> body(int status, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", status);
        body.put("message", message);
        return body;
    }
}
