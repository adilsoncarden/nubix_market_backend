package com.nubix.market.module.auth.dto;

public class VerficarCodigoRequest {
    private String codigo;
    private String email;

    public VerficarCodigoRequest() {
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
