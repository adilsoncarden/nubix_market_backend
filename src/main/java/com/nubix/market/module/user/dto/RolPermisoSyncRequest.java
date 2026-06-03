package com.nubix.market.module.user.dto;

import java.util.ArrayList;
import java.util.List;

public class RolPermisoSyncRequest {

    private List<Integer> permisoIds = new ArrayList<>();

    public List<Integer> getPermisoIds() {
        return permisoIds;
    }

    public void setPermisoIds(List<Integer> permisoIds) {
        this.permisoIds = permisoIds != null ? permisoIds : new ArrayList<>();
    }
}
