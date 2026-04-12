package com.nubix.market.services;

import com.nubix.market.dto.AuthResponse;
import com.nubix.market.dto.LoginRequest;
import com.nubix.market.dto.RegisterRequest;
import com.nubix.market.entities.Usuario;
import com.nubix.market.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public AuthResponse register(RegisterRequest request) {
        // 1. Validar que el usuario o correo no existan
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            return new AuthResponse(false, "El nombre de usuario ya está en uso", null);
        }
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            return new AuthResponse(false, "El correo ya está registrado", null);
        }

        // 2. Crear y mapear la nueva entidad
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername(request.getUsername());
        nuevoUsuario.setEmail(request.getEmail());
        // TODO a futuro: Encriptar esta contraseña antes de guardarla
        nuevoUsuario.setPassword(request.getPassword()); 

        // 3. Guardar en la base de datos
        usuarioRepository.save(nuevoUsuario);

        return new AuthResponse(true, "Usuario registrado exitosamente", nuevoUsuario.getUsername());
    }

    public AuthResponse login(LoginRequest request) {
        // 1. Buscar al usuario en la BD
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(request.getUsername());

        // 2. Validar si existe
        if (usuarioOpt.isEmpty()) {
            return new AuthResponse(false, "Usuario no encontrado", null);
        }

        Usuario usuario = usuarioOpt.get();

        // 3. Validar la contraseña (comparación directa)
        if (!usuario.getPassword().equals(request.getPassword())) {
            return new AuthResponse(false, "Contraseña incorrecta", null);
        }

        // 4. Login exitoso
        return new AuthResponse(true, "Login exitoso", usuario.getUsername());
    }
}