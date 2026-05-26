package com.nubix.market.services;

import com.nubix.market.dto.NotificacionRequest;
import com.nubix.market.entities.Notificacion;
import com.nubix.market.entities.Usuario;
import com.nubix.market.repositories.NotificacionRepository;
import com.nubix.market.repositories.UsuarioRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificacionService {

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Notificacion> obtenerMisNotificaciones() {
        Usuario actual = obtenerUsuarioActual();
        return notificacionRepository.findTop30ByUsuario_IdOrderByFechaDesc(actual.getId());
    }

    public Long contarNoLeidas() {
        Usuario actual = obtenerUsuarioActual();
        return notificacionRepository.countByUsuario_IdAndLeidoFalse(actual.getId());
    }

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
                request.getMensaje()
        );
    }

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

    private Usuario obtenerUsuarioActual() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no autenticado"));
    }
}

