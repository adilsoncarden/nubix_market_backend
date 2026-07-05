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
import java.util.List;

/**
 * Filtro de autenticación JWT que extrae el token Bearer, valida las claims y
 * establece el contexto de seguridad de Spring con las autoridades RBAC del usuario.
 * Sin {@code @Transactional} aquí (evita proxy CGLIB que rompe {@code GenericFilterBean.init}).
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final SecurityAuthorityService securityAuthorityService;

    /**
     * Inyecta utilidades JWT y el servicio que resuelve permisos del usuario.
     *
     * @param jwtUtils                  utilidad de generación y parseo de tokens
     * @param securityAuthorityService  servicio que carga roles y permisos desde la base de datos
     */
    public JwtAuthFilter(JwtUtils jwtUtils, SecurityAuthorityService securityAuthorityService) {
        this.jwtUtils = jwtUtils;
        this.securityAuthorityService = securityAuthorityService;
    }

    /**
     * Procesa la cabecera {@code Authorization: Bearer}, autentica al usuario si el token
     * es válido y continúa la cadena de filtros; ante token inválido limpia el contexto.
     *
     * @param request     petición HTTP entrante
     * @param response    respuesta HTTP
     * @param filterChain cadena de filtros servlet
     * @throws ServletException si el filtro siguiente lanza una excepción servlet
     * @throws IOException      si ocurre un error de E/S al procesar la petición
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
