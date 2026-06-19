package com.nubix.market.module.notification.service;

import com.nubix.market.module.notification.dto.NotificacionRequest;
import com.nubix.market.module.notification.model.Notificacion;
import com.nubix.market.module.notification.repository.NotificacionRepository;
import com.nubix.market.module.user.model.Usuario;
import com.nubix.market.module.user.repository.UsuarioRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio encargado de la lógica de negocio de las alertas internas (in-app) de la plataforma.
 */
@Service
public class NotificacionService {

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Busca las últimas 30 notificaciones asociadas al usuario logueado en la sesión.
     *
     * @return Lista de objetos Notificacion.
     */
    public List<Notificacion> obtenerMisNotificaciones() {
        Usuario actual = obtenerUsuarioActual();
        return notificacionRepository.findTop30ByUsuario_IdOrderByFechaDesc(actual.getId());
    }

    /**
     * Calcula la cantidad de alertas pendientes (no leídas) del usuario actual.
     *
     * @return Número de notificaciones no leídas.
     */
    public Long contarNoLeidas() {
        Usuario actual = obtenerUsuarioActual();
        return notificacionRepository.countByUsuario_IdAndLeidoFalse(actual.getId());
    }

    /**
     * Método público utilizado mediante un Controller REST para generar nuevas alertas.
     * Si no se especifica un destino, se asume que la alerta es para el usuario creador.
     *
     * @param request Datos de la notificación solicitada.
     * @return Entidad Notificacion creada.
     * @throws RuntimeException Si falta el mensaje o el usuario destino no existe.
     */
    @Transactional
    public Notificacion crear(NotificacionRequest request) {
        if (request.getMensaje() == null || request.getMensaje().isBlank()) {
            throw new RuntimeException("Mensaje de notificación obligatorio");
        }
        Usuario target = (request.getUsuarioId() != null)
                ? usuarioRepository.findById(request.getUsuarioId())
                        .orElseThrow(() -> new RuntimeException("Usuario destino no encontrado"))
                : obtenerUsuarioActual();

        return crearInterna(
                target,
                request.getTipo() != null ? request.getTipo() : "promo",
                request.getMensaje());
    }

    /**
     * Cambia el estado de una alerta específica de "no leída" a "leída".
     * Bloquea intentos de marcar como leídas las notificaciones de otros usuarios por seguridad.
     *
     * @param id Identificador de la alerta.
     * @return Entidad Notificacion modificada.
     * @throws RuntimeException Si el usuario intenta leer una alerta que no le pertenece.
     */
    @Transactional
    public Notificacion marcarLeida(Integer id) {
        Usuario actual = obtenerUsuarioActual();
        Notificacion n = notificacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));
        if (!n.getUsuario().getId().equals(actual.getId())) {
            throw new RuntimeException("No autorizado para esta notificación");
        }
        n.setLeido(true);
        return notificacionRepository.save(n);
    }

    /**
     * Método interno utilitario utilizado por otros servicios (como Ventas o Productos) 
     * para inyectar alertas automáticamente en el sistema sin pasar por un Controller HTTP.
     *
     * @param usuario Destinatario final.
     * @param tipo Categoría o icono de la alerta.
     * @param mensaje Texto descriptivo de la alerta.
     * @return Entidad persistida en BD.
     */
    @Transactional
    public Notificacion crearInterna(Usuario usuario, String tipo, String mensaje) {
        Notificacion n = new Notificacion();
        n.setUsuario(usuario);
        n.setTipo((tipo == null || tipo.isBlank()) ? "pedido" : tipo);
        n.setMensaje(mensaje);
        n.setLeido(false);
        n.setFecha(LocalDateTime.now());
        return notificacionRepository.save(n);
    }

    /**
     * Obtiene de forma segura al usuario autenticado consultando el contexto de Spring Security.
     */
    private Usuario obtenerUsuarioActual() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no autenticado"));
    }
}
