package com.nubix.market.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nubix.market.module.auth.PasswordResetErrorCodes;
import com.nubix.market.module.auth.exception.PasswordResetCodeException;
import com.nubix.market.module.auth.model.ContraseñaResetToken;
import com.nubix.market.module.auth.repository.ReseteoContraseñaRepository;
import com.nubix.market.module.auth.service.RecuperaciónContraseñaService;
import com.nubix.market.module.notification.service.EmailService;
import com.nubix.market.module.user.model.Usuario;
import com.nubix.market.module.user.repository.UsuarioRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class RecuperacionContrasenaServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ReseteoContraseñaRepository reseteoContraseñaRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private RecuperaciónContraseñaService service;

    @Test
    void verificarCodigo_expiredToken_returnsCodeExpired() {
        Usuario usuario = new Usuario();
        usuario.setEmail("user@test.com");

        ContraseñaResetToken token = new ContraseñaResetToken();
        token.setUsuario(usuario);
        token.setCodigo("123456");
        token.setFechaExpiracion(LocalDateTime.now().minusMinutes(1));
        token.setUtilizado(false);

        when(usuarioRepository.findByEmail("user@test.com")).thenReturn(Optional.of(usuario));
        when(reseteoContraseñaRepository.findTopByUsuarioAndUtilizadoFalseOrderByIdDesc(usuario))
                .thenReturn(Optional.of(token));

        PasswordResetCodeException ex = assertThrows(
                PasswordResetCodeException.class,
                () -> service.verificarCodigo("user@test.com", "123456"));

        assertEquals(PasswordResetErrorCodes.CODE_EXPIRED, ex.getErrorCode());
        verify(reseteoContraseñaRepository).save(token);
    }

    @Test
    void verificarCodigo_wrongCode_returnsInvalidCode() {
        Usuario usuario = new Usuario();
        usuario.setEmail("user@test.com");

        ContraseñaResetToken token = new ContraseñaResetToken();
        token.setUsuario(usuario);
        token.setCodigo("123456");
        token.setFechaExpiracion(LocalDateTime.now().plusMinutes(3));
        token.setUtilizado(false);

        when(usuarioRepository.findByEmail("user@test.com")).thenReturn(Optional.of(usuario));
        when(reseteoContraseñaRepository.findTopByUsuarioAndUtilizadoFalseOrderByIdDesc(usuario))
                .thenReturn(Optional.of(token));

        PasswordResetCodeException ex = assertThrows(
                PasswordResetCodeException.class,
                () -> service.verificarCodigo("user@test.com", "000000"));

        assertEquals(PasswordResetErrorCodes.INVALID_CODE, ex.getErrorCode());
        verify(reseteoContraseñaRepository, never()).save(any());
    }
}
