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
 * Clase principal de configuración de seguridad de la aplicación (Spring Security).
 * Define las políticas de CORS, el manejo de sesiones (Stateless para JWT),
 * el enrutamiento público/privado y registra los filtros personalizados de autenticación y autorización.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final RbacAuthorizationFilter rbacAuthorizationFilter;
    private final JsonAccessDeniedHandler jsonAccessDeniedHandler;

    /**
     * Constructor para inyectar los filtros personalizados y manejadores de error.
     *
     * @param jwtAuthFilter           Filtro que valida el token JWT en cada petición.
     * @param rbacAuthorizationFilter Filtro que valida los permisos del usuario contra la ruta solicitada.
     * @param jsonAccessDeniedHandler Manejador que devuelve un JSON cuando se deniega el acceso.
     */
    public SecurityConfig(
            JwtAuthFilter jwtAuthFilter,
            RbacAuthorizationFilter rbacAuthorizationFilter,
            JsonAccessDeniedHandler jsonAccessDeniedHandler) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.rbacAuthorizationFilter = rbacAuthorizationFilter;
        this.jsonAccessDeniedHandler = jsonAccessDeniedHandler;
    }

    /**
     * Orígenes permitidos para peticiones CORS, inyectados desde la configuración (application.properties).
     */
    @Value("${cors.allowed-origins:http://localhost:5173}")
    private String allowedOrigins;

    /**
     * Configura la cadena de filtros de seguridad (SecurityFilterChain) que interceptará
     * todas las peticiones HTTP entrantes.
     *
     * @param http El constructor de seguridad web de Spring.
     * @return La cadena de filtros configurada.
     * @throws Exception Si ocurre un error al construir la configuración.
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
     * Define la configuración de Cross-Origin Resource Sharing (CORS).
     * Permite que el frontend (ej. React en el puerto 5173) pueda consumir la API
     * sin ser bloqueado por las políticas del navegador.
     *
     * @return La fuente de configuración CORS.
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
     * Define el algoritmo de encriptación que se utilizará para hashear y verificar contraseñas.
     *
     * @return Una instancia de BCryptPasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
