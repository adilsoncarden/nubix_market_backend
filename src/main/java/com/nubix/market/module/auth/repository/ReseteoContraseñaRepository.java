package com.nubix.market.module.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.nubix.market.module.auth.model.ContraseñaResetToken;
import com.nubix.market.module.user.model.Usuario;
import java.util.Optional;

/**
 * Repositorio JPA para tokens de restablecimiento de contraseña.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@Repository
public interface ReseteoContraseñaRepository extends JpaRepository<ContraseñaResetToken, Long> {
    /**
     * Busca token activo más reciente.
     * @param usuario Usuario propietario.
     * @return resultado de la operación
     */
    Optional<ContraseñaResetToken> findTopByUsuarioAndUtilizadoFalseOrderByIdDesc(Usuario usuario);
}
