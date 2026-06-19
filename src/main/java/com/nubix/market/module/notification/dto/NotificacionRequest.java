package com.nubix.market.module.notification.dto;

/**
 * Objeto de transferencia de datos (DTO) utilizado para crear y enviar una nueva 
 * alerta o notificación interna (in-app) a un usuario específico.
 */
public class NotificacionRequest {

    /** El identificador del usuario que recibirá la notificación. */
    private Integer usuarioId;

    /** El tipo o categoría de la notificación (ej. "SISTEMA", "COMPRA", "PROMOCION"). */
    private String tipo;

    /** El texto descriptivo que leerá el usuario en su panel de notificaciones. */
    private String mensaje;

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
