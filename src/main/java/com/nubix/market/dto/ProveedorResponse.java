package com.nubix.market.dto;

public class ProveedorResponse {
    private Integer id;
    private String ruc;
    private String nombre;
    private String telefono;
    private String email;
    
    public ProveedorResponse() {
    }

    public ProveedorResponse(Integer id, String ruc, String nombre, String telefono, String email) {
        this.id = id;
        this.ruc = ruc;
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
    }

    //Getters and Setters
    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}
    public String getRuc() {return ruc;}
    public void setRuc(String ruc) {this.ruc = ruc;}
    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public String getTelefono() {return telefono;}
    public void setTelefono(String telefono) {this.telefono = telefono;}
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
}
