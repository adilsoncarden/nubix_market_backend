package com.nubix.market.module.user.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO de respuesta con los identificadores de permisos asignados a un rol.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
public class RolPermisoIdsResponse {

    /** Lista de ids de permiso actualmente asignados al rol. */
    private List<Integer> permisoIds = new ArrayList<>();

    /** Constructor por defecto. */
    public RolPermisoIdsResponse() {
    }

    /**
     * Crea una respuesta con la lista de ids de permiso.
     *
     * @param permisoIds lista de identificadores; {@code null} se trata como lista vacía
     */
    public RolPermisoIdsResponse(List<Integer> permisoIds) {
        this.permisoIds = permisoIds != null ? permisoIds : new ArrayList<>();
    }

    public List<Integer> getPermisoIds() {
        return permisoIds;
    }

    public void setPermisoIds(List<Integer> permisoIds) {
        this.permisoIds = permisoIds != null ? permisoIds : new ArrayList<>();
    }
}
