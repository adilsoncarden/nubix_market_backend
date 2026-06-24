package com.nubix.market.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;
import java.util.List;

/**
 * Configuración central de Spring Security para Nubix Market: CORS, sesión stateless,
 * cadena de filtros JWT/RBAC, rutas públicas y codificación de contraseñas BCrypt.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final RbacAuthorizationFilter rbacAuthorizationFilter;
    private final JsonAccessDeniedHandler jsonAccessDeniedHandler;

    /**
     * Inyecta los filtros de autenticación/autorización y el manejador de acceso denegado JSON.
     *
     * @param jwtAuthFilter              filtro que valida tokens Bearer JWT
     * @param rbacAuthorizationFilter    filtro que aplica permisos granulares por ruta
     * @param jsonAccessDeniedHandler    respuestas 403 en JSON para acceso denegado
     */
    public SecurityConfig(
            JwtAuthFilter jwtAuthFilter,
            RbacAuthorizationFilter rbacAuthorizationFilter,
            JsonAccessDeniedHandler jsonAccessDeniedHandler) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.rbacAuthorizationFilter = rbacAuthorizationFilter;
        this.jsonAccessDeniedHandler = jsonAccessDeniedHandler;
    }

    /** Orígenes permitidos para CORS, separados por coma ({@code cors.allowed-origins}). */
    @Value("${cors.allowed-origins:http://localhost:5173}")
    private String allowedOrigins;

    /**
     * Define la cadena de filtros HTTP: CSRF deshabilitado, sin sesión, reglas de
     * autorización por ruta y filtros JWT/RBAC antes del filtro de autenticación por usuario.
     *
     * @param http configurador de seguridad HTTP de Spring
     * @return cadena de filtros de seguridad construida
     * @throws Exception si la configuración de {@link HttpSecurity} falla
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/register",
                                "/api/auth/login",
                                "/api/auth/admin-login",
                                "/api/auth/forgot-password",
                                "/api/auth/verify-code",
                                "/api/auth/reset-password",
                                "/api/catalogo/**",
                                "/api/public/**",
                                "/error")
                        .permitAll()
                        .requestMatchers("/api/auth/admin-permisos")
                        .authenticated()
                        .requestMatchers("/api/permisos", "/api/permisos/**", "/api/roles", "/api/roles/**")
                        .authenticated()
                        .requestMatchers("/api/admin/**")
                        .authenticated()
                        .requestMatchers(
                                "/api/ventas/checkout",
                                "/api/ventas/cargo",
                                "/api/ventas/mis-pedidos",
                                "/api/usuarios/perfil",
                                "/api/config/google-maps-key",
                                "/api/identidad/**",
                                "/api/carrito/**",
                                "/api/favoritos/**",
                                "/api/notificaciones/**",
                                "/api/email/**")
                        .hasAnyRole("CLIENTE", "ADMIN", "EMPLEADO")
                        .anyRequest().authenticated())
                .exceptionHandling(ex -> ex.accessDeniedHandler(jsonAccessDeniedHandler))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(rbacAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Registra la configuración CORS global a partir de {@link #allowedOrigins}.
     *
     * @return fuente de configuración CORS aplicada a todas las rutas ({@code /**})
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        List<String> origins = Arrays.stream(allowedOrigins.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
        configuration.setAllowedOrigins(origins);
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "Accept",
                "Cache-Control",
                "Pragma"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Codificador de contraseñas BCrypt para registro y autenticación de usuarios.
     *
     * @return implementación {@link BCryptPasswordEncoder} de {@link PasswordEncoder}
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
