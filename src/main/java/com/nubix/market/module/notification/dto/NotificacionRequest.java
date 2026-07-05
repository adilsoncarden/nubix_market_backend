package com.nubix.market.module.notification.dto;

/**
 * DTO de solicitud para crear una notificación.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
public class NotificacionRequest {
    /** Id del usuario destino. */
    private Integer usuarioId;
    /** Tipo del recurso. */
    private String tipo;
    /** Contenido del mensaje. */
    private String mensaje;

    /**
     * GetUsuarioId.
     * @return resultado de la operación
     */
    public Integer getUsuarioId() {
        return usuarioId;
    }

    /**
     * SetUsuarioId.
     * @param usuarioId Id del usuario destino.
     */
    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    /**
     * GetTipo.
     * @return resultado de la operación
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * SetTipo.
     * @param tipo Tipo del recurso.
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * GetMensaje.
     * @return resultado de la operación
     */
    public String getMensaje() {
        return mensaje;
    }

    /**
     * SetMensaje.
     * @param mensaje Contenido del mensaje.
     */
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
