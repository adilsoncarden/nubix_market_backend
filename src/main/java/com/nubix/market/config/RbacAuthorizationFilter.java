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
 * Filtro de seguridad responsable de la autorización basada en roles y permisos (RBAC).
 * Intercepta las peticiones HTTP y verifica si el usuario autenticado posee el permiso 
 * necesario para acceder a la ruta solicitada.
 */
@Component
public class RbacAuthorizationFilter extends OncePerRequestFilter {

    private final RbacPermissionResolver permissionResolver;
    private final RbacForbiddenResponseWriter forbiddenResponseWriter;

    /**
     * Constructor para la inyección de dependencias.
     *
     * @param permissionResolver      Resolutor encargado de determinar qué permiso requiere la solicitud actual.
     * @param forbiddenResponseWriter Manejador para devolver respuestas estructuradas cuando se deniega el acceso.
     */
    public RbacAuthorizationFilter(
            RbacPermissionResolver permissionResolver,
            RbacForbiddenResponseWriter forbiddenResponseWriter) {
        this.permissionResolver = permissionResolver;
        this.forbiddenResponseWriter = forbiddenResponseWriter;
    }

    /**
     * Lógica central del filtro de autorización.
     * Resuelve el permiso requerido para la ruta y lo compara con los privilegios del usuario.
     * Permite el acceso a los administradores o si la ruta no requiere permisos específicos.
     *
     * @param request La solicitud HTTP entrante.
     * @param response La respuesta HTTP saliente.
     * @param filterChain Cadena de filtros para continuar la ejecución si se concede el acceso.
     * @throws ServletException Si ocurre un error en el procesamiento del servlet.
     * @throws IOException      Si ocurre un error de entrada/salida.
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

    /**
     * Comprueba si el usuario autenticado posee el rol de administrador global.
     *
     * @param auth El objeto de autenticación actual.
     * @return {@code true} si el usuario tiene la autoridad 'ROLE_ADMIN', {@code false} en caso contrario.
     */
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
     * Mantiene la compatibilidad hacia atrás para permisos sembrados antes de la refactorización 
     * a 'gestionar:seguridad'.
     *
     * @param auth     El objeto de autenticación actual.
     * @param required El permiso que se está evaluando.
     * @return {@code true} si se requiere gestionar seguridad y el usuario tiene el permiso heredado.
     */
    private boolean hasLegacySeguridadAlias(Authentication auth, String required) {
        if (!"gestionar:seguridad".equals(required)) {
            return false;
        }
        return hasAuthority(auth, "gestionar:permisos");
    }

    /**
     * Verifica si el usuario posee una autoridad (permiso) específica en su lista de privilegios.
     *
     * @param auth      El objeto de autenticación actual.
     * @param authority El nombre del permiso requerido (ej. "ver:productos").
     * @return {@code true} si el usuario tiene el permiso, {@code false} si no.
     */
    private boolean hasAuthority(Authentication auth, String authority) {
        for (GrantedAuthority ga : auth.getAuthorities()) {
            if (authority != null && authority.equals(ga.getAuthority())) {
                return true;
            }
        }
        return false;
    }
}
