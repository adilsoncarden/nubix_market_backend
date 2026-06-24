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

/**
 * Servicio de consulta y actualización del perfil del usuario autenticado.
 * <p>
 * Obtiene el usuario desde el contexto de seguridad de Spring y expone sus datos
 * de contacto, ubicación y facturación mediante DTOs de perfil.
 * </p>
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@Service
public class UsuarioPerfilService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Obtiene el perfil del usuario actualmente autenticado.
     *
     * @return DTO {@link PerfilResponse} con los datos del perfil
     * @throws RuntimeException si no hay usuario autenticado en el contexto de seguridad
     */
    @Transactional(readOnly = true)
    public PerfilResponse obtenerPerfilActual() {
        return toResponse(obtenerUsuarioActual());
    }

    /**
     * Actualiza parcialmente el perfil del usuario autenticado.
     * <p>
     * Solo se modifican los campos presentes en la solicitud; los valores en blanco
     * se normalizan a {@code null}.
     * </p>
     *
     * @param request datos de perfil a actualizar (campos opcionales)
     * @return DTO {@link PerfilResponse} con el perfil actualizado
     * @throws RuntimeException si no hay usuario autenticado en el contexto de seguridad
     */
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
        if (request.getDniRuc() != null) {
            usuario.setDniRuc(blankToNull(request.getDniRuc()));
        }
        if (request.getNombreRazonSocial() != null) {
            usuario.setNombreRazonSocial(blankToNull(request.getNombreRazonSocial()));
        }
        return toResponse(usuarioRepository.save(usuario));
    }

    /**
     * Obtiene la entidad del usuario autenticado desde el contexto de Spring Security.
     *
     * @return entidad {@link Usuario} con su rol cargado
     * @throws RuntimeException si el usuario no está autenticado o no existe en base de datos
     */
    private Usuario obtenerUsuarioActual() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByUsernameWithRol(username)
                .orElseThrow(() -> new RuntimeException("Usuario no autenticado"));
    }

    /**
     * Convierte una cadena en blanco o solo espacios a {@code null}; en caso contrario la recorta.
     *
     * @param value valor de entrada
     * @return valor recortado o {@code null} si está en blanco
     */
    private static String blankToNull(String value) {
        return StringUtils.isBlank(value) ? null : value.trim();
    }

    /**
     * Mapea una entidad {@link Usuario} a su representación de perfil para la API.
     *
     * @param usuario entidad de usuario con datos de perfil
     * @return DTO {@link PerfilResponse} poblado con los datos del usuario
     */
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
        r.setDniRuc(usuario.getDniRuc());
        r.setNombreRazonSocial(usuario.getNombreRazonSocial());
        if (usuario.getRol() != null) {
            r.setRolNombre(usuario.getRol().getNombre());
        }
        return r;
    }
}
