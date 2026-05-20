package com.nubix.market.dto;

public class NuevaContraseñaRequest {
    private String email;
    private String nuevaContraseña;

    public NuevaContraseñaRequest() {
    }

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
    public String getNuevaContraseña() {return nuevaContraseña;}
    public void setNuevaContraseña(String nuevaContraseña) {this.nuevaContraseña = nuevaContraseña;}
}
