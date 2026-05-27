package com.nubix.market.services;

import com.nubix.market.config.JwtUtils;
import com.nubix.market.dto.AuthResponse;
import com.nubix.market.dto.LoginRequest;
import com.nubix.market.entities.Rol;
import com.nubix.market.entities.Usuario;
import com.nubix.market.repositories.RolRepository;
import com.nubix.market.repositories.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private AuthService authService;

    @Test
    void login_exito() {
        LoginRequest req = new LoginRequest();
        req.setUsername("user1");
        req.setPassword("clave123");

        Usuario u = new Usuario();
        u.setUsername("user1");
        u.setPassword("hash");
        Rol r = new Rol("CLIENTE");
        u.setRol(r);

        when(usuarioRepository.findByUsername("user1")).thenReturn(Optional.of(u));
        when(passwordEncoder.matches("clave123", "hash")).thenReturn(true);
        when(jwtUtils.generateToken("user1")).thenReturn("jwt-token");

        AuthResponse resp = authService.login(req);

        assertThat(resp.isSuccess()).isTrue();
        assertThat(resp.getToken()).isEqualTo("jwt-token");
        assertThat(resp.getRol()).isEqualTo("CLIENTE");
    }

    @Test
    void login_contrasenaIncorrecta() {
        LoginRequest req = new LoginRequest();
        req.setUsername("user1");
        req.setPassword("mala");

        Usuario u = new Usuario();
        u.setUsername("user1");
        u.setPassword("hash");
        u.setRol(new Rol("CLIENTE"));

        when(usuarioRepository.findByUsername("user1")).thenReturn(Optional.of(u));
        when(passwordEncoder.matches("mala", "hash")).thenReturn(false);

        AuthResponse resp = authService.login(req);

        assertThat(resp.isSuccess()).isFalse();
        assertThat(resp.getMessage()).containsIgnoringCase("contraseña");
    }
}
