package com.nubix.market.module.favorite.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.nubix.market.module.favorite.model.Favorito;

/**
 * Repositorio JPA para favoritos.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@Repository
public interface FavoritoRepository extends JpaRepository<Favorito, Integer> {
    /**
     * Obtiene favoritos de un usuario.
     * @param usuarioId Id del usuario destino.
     * @return resultado de la operación
     */
    List<Favorito> findAllByUsuario_Id(Integer usuarioId);

    /**
     * FindByUsuario_IdAndProducto_Id.
     * @param usuarioId Id del usuario destino.
     * @param productoId valor del parámetro
     * @return resultado de la operación
     */
    Optional<Favorito> findByUsuario_IdAndProducto_Id(Integer usuarioId, Integer productoId);

    /**
     * Verifica si producto es favorito.
     * @param usuarioId Id del usuario destino.
     * @param productoId valor del parámetro
     * @return resultado de la operación
     */
    boolean existsByUsuario_IdAndProducto_Id(Integer usuarioId, Integer productoId);

    /**
     * Elimina relación de favorito.
     * @param usuarioId Id del usuario destino.
     * @param productoId valor del parámetro
     */
    void deleteByUsuario_IdAndProducto_Id(Integer usuarioId, Integer productoId);
}
