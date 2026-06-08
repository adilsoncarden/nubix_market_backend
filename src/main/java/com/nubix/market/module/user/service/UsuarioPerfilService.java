package com.nubix.market.module.user.service;

import com.nubix.market.module.user.dto.PerfilResponse;
import com.nubix.market.module.user.dto.PerfilUpdateRequest;
import com.nubix.market.module.user.model.Usuario;
import com.nubix.market.module.user.repository.UsuarioRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioPerfilService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public PerfilResponse obtenerPerfilActual() {
        return toResponse(obtenerUsuarioActual());
    }

    @Transactional
    public PerfilResponse actualizarPerfilActual(PerfilUpdateRequest request) {
        Usuario usuario = obtenerUsuarioActual();
        if (request.getTelefono() != null) {
            usuario.setTelefono(blankToNull(request.getTelefono()));
        }
        if (request.getDireccion() != null) {
            usuario.setDireccion(blankToNull(request.getDireccion()));
        }
        if (request.getDepartamento() != null) {
            usuario.setDepartamento(blankToNull(request.getDepartamento()));
        }
        if (request.getProvincia() != null) {
            usuario.setProvincia(blankToNull(request.getProvincia()));
        }
        if (request.getDistrito() != null) {
            usuario.setDistrito(blankToNull(request.getDistrito()));
        }
        if (request.getReferencia() != null) {
            usuario.setReferencia(blankToNull(request.getReferencia()));
        }
        if (request.getLatitud() != null) {
            usuario.setLatitud(request.getLatitud());
        }
        if (request.getLongitud() != null) {
            usuario.setLongitud(request.getLongitud());
        }
        if (request.getGooglePlaceId() != null) {
            usuario.setGooglePlaceId(blankToNull(request.getGooglePlaceId()));
        }
        return toResponse(usuarioRepository.save(usuario));
    }

    private Usuario obtenerUsuarioActual() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByUsernameWithRol(username)
                .orElseThrow(() -> new RuntimeException("Usuario no autenticado"));
    }

    private static String blankToNull(String value) {
        return StringUtils.isBlank(value) ? null : value.trim();
    }

    private static PerfilResponse toResponse(Usuario usuario) {
        PerfilResponse r = new PerfilResponse();
        r.setId(usuario.getId());
        r.setUsername(usuario.getUsername());
        r.setEmail(usuario.getEmail());
        r.setTelefono(usuario.getTelefono());
        r.setDireccion(usuario.getDireccion());
        r.setDepartamento(usuario.getDepartamento());
        r.setProvincia(usuario.getProvincia());
        r.setDistrito(usuario.getDistrito());
        r.setReferencia(usuario.getReferencia());
        r.setLatitud(usuario.getLatitud());
        r.setLongitud(usuario.getLongitud());
        r.setGooglePlaceId(usuario.getGooglePlaceId());
        if (usuario.getRol() != null) {
            r.setRolNombre(usuario.getRol().getNombre());
        }
        return r;
    }
}
