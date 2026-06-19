package com.nubix.market.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

/**
 * Componente utilitario que se encarga de escribir respuestas HTTP estandarizadas 
 * de error (403 Forbidden) cuando el filtro de seguridad (RBAC) bloquea una solicitud.
 */
@Component
public class RbacForbiddenResponseWriter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Configura y escribe el cuerpo de la respuesta HTTP indicando que el acceso ha sido denegado.
     *
     * @param response El objeto de respuesta HTTP (HttpServletResponse) al que se escribirá el error JSON.
     * @throws IOException Si ocurre un error de entrada/salida durante la escritura de la respuesta.
     */
    public void write(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(), ApiErrorResponse.forbidden());
    }
}
