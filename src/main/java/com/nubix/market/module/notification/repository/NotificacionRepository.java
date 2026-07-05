package com.nubix.market.module.notification.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.nubix.market.module.notification.model.Notificacion;

/**
 * Repositorio JPA para notificaciones.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {
    /**
     * Obtiene últimas 30 notificaciones.
     * @param usuarioId Id del usuario destino.
     * @return resultado de la operación
     */
    List<Notificacion> findTop30ByUsuario_IdOrderByFechaDesc(Integer usuarioId);

    /**
     * Cuenta notificaciones no leídas.
     * @param usuarioId Id del usuario destino.
     * @return resultado de la operación
     */
    Long countByUsuario_IdAndLeidoFalse(Integer usuarioId);
}
