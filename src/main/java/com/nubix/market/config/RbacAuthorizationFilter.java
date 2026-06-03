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

@Component
public class RbacAuthorizationFilter extends OncePerRequestFilter {

    private final RbacPermissionResolver permissionResolver;

    public RbacAuthorizationFilter(RbacPermissionResolver permissionResolver) {
        this.permissionResolver = permissionResolver;
    }

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

        response.sendError(HttpServletResponse.SC_FORBIDDEN,
                "No tiene permisos para realizar esta acción");
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

    /** Compatibilidad con permisos sembrados antes de gestionar:seguridad */
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
