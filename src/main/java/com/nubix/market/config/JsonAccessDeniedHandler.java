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
 * Manejador de acceso denegado de Spring Security que responde en JSON cuando
 * un usuario autenticado carece de permisos, en lugar de redirigir o devolver HTML.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@Component
public class JsonAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Escribe una respuesta HTTP 403 con cuerpo JSON estandarizado.
     *
     * @param request               petición HTTP que provocó el acceso denegado
     * @param response              respuesta HTTP donde se escribe el error
     * @param accessDeniedException excepción de Spring Security (no se expone al cliente)
     * @throws IOException si falla la escritura del cuerpo JSON
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
