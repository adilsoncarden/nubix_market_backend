package com.nubix.market.module.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.nubix.market.module.auth.model.ContraseñaResetToken;
import com.nubix.market.module.user.model.Usuario;
import java.util.Optional;

/**
 * Repositorio de Spring Data JPA para gestionar las operaciones de base de datos 
 * de la entidad {@link ContraseñaResetToken}.
 */
@Repository
public interface ReseteoContraseñaRepository extends JpaRepository<ContraseñaResetToken, Long> {

    /**
     * Busca el token más reciente generado para un usuario específico que aún no haya sido utilizado.
     * Se ordena de forma descendente por ID para garantizar que se recupere el último intento.
     *
     * @param usuario El usuario del cual se quiere buscar el token pendiente.
     * @return Un {@link Optional} que contiene el token si existe y cumple las condiciones, o vacío en caso contrario.
     */
    Optional<ContraseñaResetToken> findTopByUsuarioAndUtilizadoFalseOrderByIdDesc(Usuario usuario);
}
