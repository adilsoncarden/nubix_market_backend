package com.nubix.market.module.user.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO utilizado al guardar la matriz de permisos de un Rol.
 * Contiene el listado final de todos los IDs de permisos que el rol debe tener.
 */
public class RolPermisoSyncRequest {

    private List<Integer> permisoIds = new ArrayList<>();

    public List<Integer> getPermisoIds() {
        return permisoIds;
    }

    public void setPermisoIds(List<Integer> permisoIds) {
        this.permisoIds = permisoIds != null ? permisoIds : new ArrayList<>();
    }
}
