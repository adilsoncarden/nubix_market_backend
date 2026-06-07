package com.nubix.market.module.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.nubix.market.module.cart.model.Carrito;
import com.nubix.market.module.user.model.Usuario;
import java.util.Optional;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Integer> {

    Optional<Carrito> findByUsuario(Usuario usuario);

    @Query("""
            SELECT DISTINCT c FROM Carrito c
            LEFT JOIN FETCH c.items i
            LEFT JOIN FETCH i.producto p
            LEFT JOIN FETCH p.imagen
            LEFT JOIN FETCH p.categoria
            WHERE c.usuario.id = :usuarioId
            """)
    Optional<Carrito> findByUsuarioIdWithItems(Integer usuarioId);
}
