package com.nubix.market.module.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.nubix.market.module.auth.model.ContraseñaResetToken;
import com.nubix.market.module.user.model.Usuario;
import java.util.Optional;

@Repository
public interface ReseteoContraseñaRepository extends JpaRepository<ContraseñaResetToken, Long> {
    Optional<ContraseñaResetToken> findTopByUsuarioAndUtilizadoFalseOrderByIdDesc(Usuario usuario);
}
