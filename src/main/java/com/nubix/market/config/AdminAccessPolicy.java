package com.nubix.market.config;

import com.nubix.market.module.user.model.Permiso;
import com.nubix.market.module.user.model.Rol;
import com.nubix.market.module.user.model.Usuario;
import com.nubix.market.module.user.repository.RolRepository;
import org.springframework.stereotype.Component;

/**
 * Reglas de acceso al panel administrativo (login y validaciones relacionadas).
 */
@Component
public class AdminAccessPolicy {

    public static final String PERMISO_PANEL = "ver:dashboard";

    private final RolRepository rolRepository;

    public AdminAccessPolicy(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    public boolean canAccessAdminPanel(Usuario usuario) {
        if (usuario == null || usuario.getRol() == null) {
            return false;
        }
        Rol rol = usuario.getRol();
        String nombreRol = rol.getNombre();
        if (nombreRol != null && "CLIENTE".equalsIgnoreCase(nombreRol.trim())) {
            return false;
        }
        if (rolTienePermiso(rol.getId(), PERMISO_PANEL)) {
            return true;
        }
        return nombreRol != null && !nombreRol.isBlank();
    }

    private boolean rolTienePermiso(Integer rolId, String permisoNombre) {
        if (rolId == null || permisoNombre == null) {
            return false;
        }
        return rolRepository.findByIdWithPermisos(rolId)
                .map(rol -> rol.getPermisos().stream()
                        .map(Permiso::getNombre)
                        .anyMatch(n -> permisoNombre.equals(n)))
                .orElse(false);
    }
}
