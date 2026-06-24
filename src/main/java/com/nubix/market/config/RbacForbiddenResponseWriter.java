package com.nubix.market.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

/**
 * Componente reutilizable que escribe respuestas HTTP 403 en JSON cuando el filtro
 * RBAC deniega el acceso a un recurso administrativo de Nubix Market.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@Component
public class RbacForbiddenResponseWriter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Configura el estado, tipo de contenido y cuerpo JSON de acceso denegado.
     *
     * @param response respuesta HTTP servlet donde se escribe el error
     * @throws IOException si falla la serialización o escritura del JSON
     */
    public void write(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(), ApiErrorResponse.forbidden());
    }
}
