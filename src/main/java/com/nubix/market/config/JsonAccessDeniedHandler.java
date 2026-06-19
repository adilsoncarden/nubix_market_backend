package com.nubix.market.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

/**
 * Manejador personalizado para excepciones de acceso denegado (HTTP 403).
 * Intercepta los casos en los que un usuario autenticado intenta acceder a un 
 * recurso para el cual no tiene los permisos necesarios, devolviendo una 
 * respuesta estructurada en formato JSON en lugar de la página HTML por defecto de Spring.
 */
@Component
public class JsonAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Procesa la excepción de acceso denegado y construye la respuesta HTTP.
     * Configura el estado, el tipo de contenido y escribe el cuerpo del error utilizando {@link ApiErrorResponse}.
     *
     * @param request La solicitud HTTP que provocó la excepción.
     * @param response La respuesta HTTP que se enviará al cliente.
     * @param accessDeniedException La excepción de seguridad capturada por Spring.
     * @throws IOException Si ocurre un error de entrada/salida al escribir el cuerpo de la respuesta.
     */
    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(), ApiErrorResponse.forbidden());
    }
}
