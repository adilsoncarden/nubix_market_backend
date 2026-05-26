package com.nubix.market.repositories;

import com.nubix.market.entities.Notificacion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {
    List<Notificacion> findTop30ByUsuario_IdOrderByFechaDesc(Integer usuarioId);

    Long countByUsuario_IdAndLeidoFalse(Integer usuarioId);
}

