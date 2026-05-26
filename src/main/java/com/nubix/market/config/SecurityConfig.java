package com.nubix.market.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
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

@Configuration
@EnableWebSecurity
public class SecurityConfig {   

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Habilitamos CORS con la configuración que definimos abajo
                .cors(Customizer.withDefaults())
                // Deshabilitamos CSRF porque usamos tokens JWT
                .csrf(csrf -> csrf.disable())
                // Configuración de sesión sin estado (Stateless)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Rutas públicas de autenticación
                        .requestMatchers(
                                "/api/auth/**",
                                "/api/catalogo/**",
                                "/api/ventas/checkout",
                                "/uploads/**")
                        .permitAll()

                        // IMPORTANTE: Permitir el endpoint de error por defecto de Spring Boot.
                        // Esto evita que un error 404 o 500 se disfrace de un error 403 (Forbidden).
                        .requestMatchers("/error").permitAll()

                        // Cualquier otra ruta requerirá autenticación
                        .anyRequest().authenticated()
                )
                // Filtro JWT antes del filtro de autenticación por usuario/contraseña
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configuración global de CORS para que el navegador permita
     * la conexión desde el puerto 5173 (Vite)
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Permitimos el origen de tu frontend en desarrollo
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        // Métodos HTTP permitidos
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Headers permitidos y necesarios para intercambiar el Token JWT
        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "Accept",
                "Cache-Control",
                "Pragma"));
        // Permitir envío de credenciales/cookies si fuera necesario
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