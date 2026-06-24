package com.nubix.market.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

/**
 * Filtro de autorización RBAC que evalúa el permiso requerido para cada ruta
 * administrativa y deniega el acceso con respuesta JSON 403 cuando el usuario
 * autenticado no posee la autoridad correspondiente.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@Component
public class RbacAuthorizationFilter extends OncePerRequestFilter {

    private final RbacPermissionResolver permissionResolver;
    private final RbacForbiddenResponseWriter forbiddenResponseWriter;

    /**
     * Inyecta el resolvedor de permisos por URI y el escritor de respuestas prohibidas.
     *
     * @param permissionResolver         mapea peticiones HTTP a nombres de permiso RBAC
     * @param forbiddenResponseWriter    serializa el cuerpo JSON 403 estandarizado
     */
    public RbacAuthorizationFilter(
            RbacPermissionResolver permissionResolver,
            RbacForbiddenResponseWriter forbiddenResponseWriter) {
        this.permissionResolver = permissionResolver;
        this.forbiddenResponseWriter = forbiddenResponseWriter;
    }

    /**
     * Resuelve el permiso exigido por la petición y permite o bloquea el acceso
     * según las autoridades del usuario en el contexto de seguridad.
     *
     * @param request     petición HTTP entrante
     * @param response    respuesta HTTP (se escribe 403 si no hay permiso)
     * @param filterChain cadena de filtros servlet
     * @throws ServletException si el filtro siguiente lanza una excepción servlet
     * @throws IOException      si falla la escritura de la respuesta prohibida
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String required = permissionResolver.resolve(request);
        if (required == null) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            filterChain.doFilter(request, response);
            return;
        }

        if (isAdmin(auth) || hasAuthority(auth, required) || hasLegacySeguridadAlias(auth, required)) {
            filterChain.doFilter(request, response);
            return;
        }

        forbiddenResponseWriter.write(response);
    }

    private boolean isAdmin(Authentication auth) {
        for (GrantedAuthority ga : auth.getAuthorities()) {
            String a = ga.getAuthority();
            if (a != null && "ROLE_ADMIN".equalsIgnoreCase(a)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Compatibilidad con permisos sembrados antes de {@code gestionar:seguridad}:
     * acepta {@code gestionar:permisos} como alias legacy.
     */
    private boolean hasLegacySeguridadAlias(Authentication auth, String required) {
        if (!"gestionar:seguridad".equals(required)) {
            return false;
        }
        return hasAuthority(auth, "gestionar:permisos");
    }

    private boolean hasAuthority(Authentication auth, String authority) {
        for (GrantedAuthority ga : auth.getAuthorities()) {
            if (authority != null && authority.equals(ga.getAuthority())) {
                return true;
            }
        }
        return false;
    }
}
