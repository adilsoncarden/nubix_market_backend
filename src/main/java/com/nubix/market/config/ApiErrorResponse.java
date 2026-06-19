package com.nubix.market.config;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Clase de utilidad para estandarizar las respuestas de error de la API.
 * Proporciona métodos para construir cuerpos de respuesta en formato JSON (Map)
 * de manera estructurada y consistente para el frontend.
 */
public final class ApiErrorResponse {

    /**
     * Mensaje por defecto utilizado cuando un usuario intenta acceder a un recurso
     * o endpoint sin tener los privilegios adecuados.
     */
    public static final String FORBIDDEN_MESSAGE =
            "No tiene los permisos necesarios para realizar esta acción.";

    /**
     * Constructor privado para evitar la instanciación de esta clase de utilidad.
     */
    private ApiErrorResponse() {
    }

    /**
     * Construye una respuesta estándar para el error HTTP 403 (Forbidden).
     *
     * @return Un mapa que representa el JSON de la respuesta con el estado 403 y el mensaje de error por defecto.
     */
    public static Map<String, Object> forbidden() {
        return body(403, FORBIDDEN_MESSAGE);
    }

    /**
     * Crea un cuerpo de respuesta personalizado con un código de estado y un mensaje específico.
     * Utiliza un {@link LinkedHashMap} para garantizar que las claves se mantengan en el orden de inserción.
     *
     * @param status  El código de estado HTTP (ej. 403, 404, 500).
     * @param message El mensaje descriptivo del error que se mostrará al cliente.
     * @return Un mapa estructurado con las propiedades "status" y "message".
     */
    public static Map<String, Object> body(int status, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", status);
        body.put("message", message);
        return body;
    }
}
