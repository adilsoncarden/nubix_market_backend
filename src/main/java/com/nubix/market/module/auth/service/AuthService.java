package com.nubix.market.module.auth.service;

import com.nubix.market.config.AdminAccessPolicy;
import com.nubix.market.config.JwtUtils;
import com.nubix.market.config.SecurityAuthorityService;
import com.nubix.market.module.auth.AuthMessages;
import com.nubix.market.module.auth.dto.AuthResponse;
import com.nubix.market.module.auth.dto.LoginRequest;
import com.nubix.market.module.auth.dto.RegisterRequest;
import com.nubix.market.module.user.model.Rol;
import com.nubix.market.module.user.model.Usuario;
import com.nubix.market.module.user.repository.RolRepository;
import com.nubix.market.module.user.repository.UsuarioRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UsuarioRepository usuarioRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final RolRepository rolRepository;
    private final AdminAccessPolicy adminAccessPolicy;
    private final SecurityAuthorityService securityAuthorityService;

    public AuthService(
            UsuarioRepository usuarioRepository,
            JwtUtils jwtUtils,
            PasswordEncoder passwordEncoder,
            RolRepository rolRepository,
            AdminAccessPolicy adminAccessPolicy,
            SecurityAuthorityService securityAuthorityService) {
        this.usuarioRepository = usuarioRepository;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.rolRepository = rolRepository;
        this.adminAccessPolicy = adminAccessPolicy;
        this.securityAuthorityService = securityAuthorityService;
    }

    public AuthResponse register(RegisterRequest request) {
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            return new AuthResponse(false, "El nombre de usuario ya existe", null);
        }
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            return new AuthResponse(false, "El correo electronico ya existe", null);
        }

        Rol rol = this.rolRepository.findByNombre("CLIENTE")
                .orElseThrow(() -> new RuntimeException("Rol CLIENTE no encontrado en la base de datos"));

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername(request.getUsername());
        nuevoUsuario.setEmail(request.getEmail());
        nuevoUsuario.setRol(rol);
        nuevoUsuario.setPassword(passwordEncoder.encode(request.getPassword()));

        usuarioRepository.save(nuevoUsuario);

        return new AuthResponse(true, "Usuario registrado exitosamente", nuevoUsuario.getUsername());
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        Optional<Usuario> usuarioOpt = buscarPorCredenciales(request);

        if (usuarioOpt.isEmpty()) {
            log.warn("Login fallido: usuario no encontrado");
            return new AuthResponse(false, AuthMessages.CREDENCIALES_INVALIDAS, null);
        }

        Usuario usuario = usuarioOpt.get();
        if (!validarPassword(usuario, request.getPassword())) {
            return new AuthResponse(false, AuthMessages.CREDENCIALES_INVALIDAS, null);
        }

        String rolNombre = obtenerNombreRol(usuario);
        if (rolNombre == null) {
            log.error("Login fallido: usuario {} sin rol asignado", usuario.getUsername());
            return new AuthResponse(
                    false,
                    "Su cuenta no tiene un rol válido. Contacte al administrador.",
                    null);
        }

        String token = jwtUtils.generateToken(usuario.getUsername(), rolNombre);
        log.info("Login exitoso para usuario {} (rol {})", usuario.getUsername(), rolNombre);
        return new AuthResponse(
                true,
                "Login exitoso",
                usuario.getId(),
                usuario.getUsername(),
                token,
                rolNombre);
    }

    @Transactional(readOnly = true)
    public AuthResponse adminLogin(LoginRequest request) {
        Optional<Usuario> usuarioOpt = buscarPorCredenciales(request);

        if (usuarioOpt.isEmpty()) {
            log.warn("Admin login fallido: usuario no encontrado");
            return new AuthResponse(false, AuthMessages.CREDENCIALES_INVALIDAS, null);
        }

        Usuario usuario = usuarioOpt.get();
        if (!validarPassword(usuario, request.getPassword())) {
            return new AuthResponse(false, AuthMessages.CREDENCIALES_INVALIDAS, null);
        }

        if (!adminAccessPolicy.canAccessAdminPanel(usuario)) {
            log.warn(
                    "Admin login denegado: usuario {} sin permiso de panel ni rol interno",
                    usuario.getUsername());
            return new AuthResponse(
                    false,
                    "Acceso denegado: no tiene permisos para ingresar al panel administrativo",
                    null);
        }

        String rolNombre = obtenerNombreRol(usuario);
        if (rolNombre == null) {
            log.error("Admin login fallido: usuario {} sin rol asignado", usuario.getUsername());
            return new AuthResponse(
                    false,
                    "Su cuenta no tiene un rol válido. Contacte al administrador.",
                    null);
        }

        String token = jwtUtils.generateToken(usuario.getUsername(), rolNombre);
        List<String> permisos = cargarPermisosParaRespuesta(usuario.getUsername(), rolNombre);

        log.info("Admin login exitoso para {} (rol {})", usuario.getUsername(), rolNombre);
        AuthResponse response = new AuthResponse(
                true,
                "Bienvenido al panel de administración",
                usuario.getId(),
                usuario.getUsername(),
                token,
                rolNombre);
        response.setPermisos(permisos);
        return response;
    }

    private Optional<Usuario> buscarPorCredenciales(LoginRequest request) {
        if (!StringUtils.isBlank(request.getUsername())) {
            Optional<Usuario> porUsername =
                    usuarioRepository.findByUsernameWithRol(request.getUsername().trim());
            if (porUsername.isPresent()) {
                return porUsername;
            }
        }
        if (!StringUtils.isBlank(request.getEmail())) {
            return usuarioRepository.findByEmailWithRol(request.getEmail().trim());
        }
        return Optional.empty();
    }

    private boolean validarPassword(Usuario usuario, String passwordPlano) {
        if (passwordPlano == null || usuario.getPassword() == null) {
            return false;
        }
        try {
            boolean ok = passwordEncoder.matches(passwordPlano, usuario.getPassword());
            if (!ok) {
                log.warn("Login fallido: contraseña incorrecta para {}", usuario.getUsername());
            }
            return ok;
        } catch (Exception e) {
            log.warn("Error al validar contraseña para {}", usuario.getUsername(), e);
            return false;
        }
    }

    private String obtenerNombreRol(Usuario usuario) {
        if (usuario.getRol() == null || usuario.getRol().getNombre() == null) {
            return null;
        }
        return usuario.getRol().getNombre().trim();
    }

    private List<String> cargarPermisosParaRespuesta(String username, String rolNombre) {
        try {
            return securityAuthorityService.loadAuthorities(username, rolNombre).stream()
                    .map(SimpleGrantedAuthority::getAuthority)
                    .filter(a -> a != null && !a.startsWith("ROLE_"))
                    .sorted()
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("No se pudieron cargar permisos para {}: {}", username, e.getMessage(), e);
            return Collections.emptyList();
        }
    }
}
