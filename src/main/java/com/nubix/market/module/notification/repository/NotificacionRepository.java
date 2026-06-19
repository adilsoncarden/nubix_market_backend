package com.nubix.market.module.notification.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.nubix.market.module.notification.model.Notificacion;

/**
 * Repositorio de Spring Data JPA para gestionar las entidades de Notificacion.
 * Proporciona métodos optimizados para cargar el historial del usuario y contabilizar las alertas pendientes.
 */
@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {

    /**
     * Recupera las últimas 30 notificaciones de un usuario específico, 
     * ordenadas desde la más reciente hasta la más antigua.
     * Limitar a 30 previene problemas de memoria y lentitud en la carga del panel frontal.
     *
     * @param usuarioId El ID del usuario.
     * @return Lista de notificaciones recientes.
     */
    List<Notificacion> findTop30ByUsuario_IdOrderByFechaDesc(Integer usuarioId);

    /**
     * Contabiliza cuántas notificaciones tiene el usuario marcadas como "No leídas" (leido = false).
     * Ideal para mostrar el "globo rojo" en la campana de alertas en la interfaz gráfica.
     *
     * @param usuarioId El ID del usuario.
     * @return El número total de alertas pendientes por leer.
     */
    Long countByUsuario_IdAndLeidoFalse(Integer usuarioId);
}
