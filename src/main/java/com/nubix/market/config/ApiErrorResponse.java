package com.nubix.market.config;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Utilidad para construir cuerpos de respuesta JSON estandarizados ante errores HTTP
 * en la API de Nubix Market, especialmente respuestas 403 por falta de permisos RBAC.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
public final class ApiErrorResponse {

    /** Mensaje por defecto cuando el usuario no tiene permisos suficientes. */
    public static final String FORBIDDEN_MESSAGE =
            "No tiene los permisos necesarios para realizar esta acción.";

    private ApiErrorResponse() {
    }

    /**
     * Construye el cuerpo JSON estándar para una respuesta HTTP 403 Forbidden.
     *
     * @return mapa con claves {@code status} y {@code message} listo para serializar
     */
    public static Map<String, Object> forbidden() {
        return body(403, FORBIDDEN_MESSAGE);
    }

    /**
     * Construye un cuerpo de error JSON con código de estado y mensaje personalizados.
     *
     * @param status  código HTTP numérico (por ejemplo, 403)
     * @param message mensaje descriptivo para el cliente
     * @return mapa ordenado con {@code status} y {@code message}
     */
    public static Map<String, Object> body(int status, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", status);
        body.put("message", message);
        return body;
    }
}
