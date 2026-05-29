package com.nubix.market.module.notification.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.nubix.market.module.notification.model.Notificacion;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {
    List<Notificacion> findTop30ByUsuario_IdOrderByFechaDesc(Integer usuarioId);

    Long countByUsuario_IdAndLeidoFalse(Integer usuarioId);
}
