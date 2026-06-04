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

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final RbacAuthorizationFilter rbacAuthorizationFilter;
    private final JsonAccessDeniedHandler jsonAccessDeniedHandler;

    public SecurityConfig(
            JwtAuthFilter jwtAuthFilter,
            RbacAuthorizationFilter rbacAuthorizationFilter,
            JsonAccessDeniedHandler jsonAccessDeniedHandler) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.rbacAuthorizationFilter = rbacAuthorizationFilter;
        this.jsonAccessDeniedHandler = jsonAccessDeniedHandler;
    }

    @Value("${cors.allowed-origins:http://localhost:5173}")
    private String allowedOrigins;

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
                                "/uploads/**",
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
                                "/api/carrito/**",
                                "/api/favoritos/**",
                                "/api/notificaciones/**",
                                "/api/email/**")
                        .hasAnyRole("CLIENTE", "ADMIN", "EMPLEADO")
                        .requestMatchers("/api/media/upload")
                        .authenticated()
                        .anyRequest().authenticated())
                .exceptionHandling(ex -> ex.accessDeniedHandler(jsonAccessDeniedHandler))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(rbacAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
