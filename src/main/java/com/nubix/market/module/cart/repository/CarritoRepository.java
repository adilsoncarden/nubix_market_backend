package com.nubix.market.module.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.nubix.market.module.cart.model.Carrito;
import com.nubix.market.module.user.model.Usuario;
import java.util.Optional;

/**
 * Repositorio JPA para carritos de compra.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Integer> {

    /**
     * FindByUsuario.
     * @param usuario Usuario propietario.
     * @return resultado de la operación
     */
    Optional<Carrito> findByUsuario(Usuario usuario);

    @Query("""
            SELECT DISTINCT c FROM Carrito c
            LEFT JOIN FETCH c.items i
            LEFT JOIN FETCH i.producto p
            LEFT JOIN FETCH p.categoria
            WHERE c.usuario.id = :usuarioId
            """)
    /**
     * Busca carrito con ítems y productos.
     * @param usuarioId Id del usuario destino.
     * @return resultado de la operación
     */
    Optional<Carrito> findByUsuarioIdWithItems(Integer usuarioId);
}
