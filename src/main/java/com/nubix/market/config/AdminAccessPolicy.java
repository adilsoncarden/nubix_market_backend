package com.nubix.market.config;

import com.nubix.market.module.user.model.Permiso;
import com.nubix.market.module.user.model.Rol;
import com.nubix.market.module.user.model.Usuario;
import com.nubix.market.module.user.repository.RolRepository;
import org.springframework.stereotype.Component;

/**
 * Reglas de acceso al panel administrativo de Nubix Market: valida si un usuario
 * puede iniciar sesión en el backoffice y acceder a rutas protegidas según su rol
 * y permisos asignados.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@Component
public class AdminAccessPolicy {

    /** Nombre del permiso requerido para visualizar el dashboard administrativo. */
    public static final String PERMISO_PANEL = "ver:dashboard";

    private final RolRepository rolRepository;

    /**
     * Crea la política con acceso al repositorio de roles para consultar permisos.
     *
     * @param rolRepository repositorio de roles del módulo de usuarios
     */
    public AdminAccessPolicy(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    /**
     * Determina si el usuario puede acceder al panel administrativo.
     * Los clientes de la tienda pública quedan excluidos; el resto debe tener
     * el permiso {@link #PERMISO_PANEL} o un rol distinto de CLIENTE con nombre válido.
     *
     * @param usuario usuario autenticado a evaluar; puede ser {@code null}
     * @return {@code true} si el usuario puede acceder al panel administrativo
     */
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
