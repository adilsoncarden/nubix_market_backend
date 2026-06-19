package com.nubix.market.config;

import com.nubix.market.module.user.model.Permiso;
import com.nubix.market.module.user.model.Rol;
import com.nubix.market.module.user.model.Usuario;
import com.nubix.market.module.user.repository.RolRepository;
import org.springframework.stereotype.Component;

/**
 * Componente de seguridad que define las reglas de acceso al panel administrativo.
 * Se encarga de validar si un usuario tiene los privilegios necesarios para 
 * acceder al dashboard basado en su rol y permisos asignados.
 */
@Component
public class AdminAccessPolicy {

    /**
     * Constante que define el nombre del permiso requerido para ver el panel de administración.
     */
    public static final String PERMISO_PANEL = "ver:dashboard";

    private final RolRepository rolRepository;

    /**
     * Constructor para la inyección de dependencias.
     * * @param rolRepository Repositorio para acceder a los datos de los roles y sus permisos.
     */
    public AdminAccessPolicy(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    /**
     * Evalúa si un usuario específico tiene autorización para acceder al panel de administración.
     * Deniega el acceso a usuarios nulos, sin rol o con el rol de "CLIENTE".
     * * @param usuario El objeto usuario cuyas credenciales y rol se van a evaluar.
     * @return {@code true} si el usuario tiene el permiso de panel o un rol válido distinto de cliente, {@code false} en caso contrario.
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

    /**
     * Verifica en la base de datos si un rol específico posee un permiso determinado.
     * * @param rolId El identificador único del rol a consultar.
     * @param permisoNombre El nombre del permiso que se desea verificar (ej. "ver:dashboard").
     * @return {@code true} si el rol existe y contiene el permiso indicado, {@code false} en caso contrario.
     */
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
