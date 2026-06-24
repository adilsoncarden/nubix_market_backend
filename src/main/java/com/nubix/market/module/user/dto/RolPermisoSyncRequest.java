package com.nubix.market.module.user.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO de solicitud para sincronizar los permisos asignados a un rol.
 * <p>
 * La lista reemplaza por completo la asignación actual; una lista vacía
 * deja el rol sin permisos.
 * </p>
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
public class RolPermisoSyncRequest {

    /** Identificadores de permisos que deben quedar asignados al rol. */
    private List<Integer> permisoIds = new ArrayList<>();

    public List<Integer> getPermisoIds() {
        return permisoIds;
    }

    public void setPermisoIds(List<Integer> permisoIds) {
        this.permisoIds = permisoIds != null ? permisoIds : new ArrayList<>();
    }
}
