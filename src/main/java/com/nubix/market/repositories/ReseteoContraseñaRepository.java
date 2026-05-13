package com.nubix.market.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.nubix.market.entities.ContraseñaResetToken;
import com.nubix.market.entities.Usuario;

import java.util.Optional;

@Repository
public interface ReseteoContraseñaRepository extends JpaRepository<ContraseñaResetToken, Long> {
    Optional<ContraseñaResetToken> findTopByUsuarioAndUtilizadoFalseOrderByIdDesc(Usuario usuario);
}
