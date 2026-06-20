package com.nubix.market.module.user.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO especializado utilizado por el frontend para marcar automáticamente los "checkboxes" 
 * de permisos que un rol ya posee asignados.
 */
public class RolPermisoIdsResponse {

    private List<Integer> permisoIds = new ArrayList<>();

    public RolPermisoIdsResponse() {
    }

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
