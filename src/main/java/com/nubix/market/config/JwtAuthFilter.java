package com.nubix.market.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

/**
 * Filtro de seguridad que intercepta cada solicitud HTTP para validar el token JWT.
 * Extrae el token de la cabecera de autorización, verifica la identidad del usuario y 
 * establece sus credenciales y roles en el contexto de seguridad de Spring.
 * <p>
 * Nota arquitectónica: Se evita deliberadamente el uso de @Transactional en esta clase 
 * para prevenir la creación de un proxy CGLIB que rompe el ciclo de vida e inicialización 
 * de GenericFilterBean.
 * </p>
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final SecurityAuthorityService securityAuthorityService;

    /**
     * Constructor para la inyección de dependencias.
     *
     * @param jwtUtils Utilidad para la extracción y validación de claims del token JWT.
     * @param securityAuthorityService Servicio encargado de cargar los permisos y roles (authorities) del usuario.
     */
    public JwtAuthFilter(JwtUtils jwtUtils, SecurityAuthorityService securityAuthorityService) {
        this.jwtUtils = jwtUtils;
        this.securityAuthorityService = securityAuthorityService;
    }

    /**
     * Lógica principal del filtro que se ejecuta una vez por cada petición HTTP.
     * Busca la cabecera "Authorization", extrae el token Bearer y, si es válido, 
     * autentica al usuario en el sistema para esa solicitud específica.
     *
     * @param request La solicitud HTTP entrante.
     * @param response La respuesta HTTP saliente.
     * @param filterChain La cadena de filtros para continuar con el flujo de la petición tras la validación.
     * @throws ServletException Si ocurre un error interno en el manejo del filtro o servlet.
     * @throws IOException Si ocurre un error de entrada/salida durante el procesamiento.
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                String username = jwtUtils.extractUsername(token);
                String rolClaim = jwtUtils.extractRol(token);
                if (username != null) {
                    var authorities = securityAuthorityService.loadAuthorities(username, rolClaim);
                    var auth = new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (Exception ignored) {
                SecurityContextHolder.clearContext();
            }
        }
        filterChain.doFilter(request, response);
    }
}
