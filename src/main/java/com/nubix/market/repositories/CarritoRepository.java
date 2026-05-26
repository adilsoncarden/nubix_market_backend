package com.nubix.market.repositories;

import com.nubix.market.entities.Carrito;
import com.nubix.market.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Integer> {

    Optional<Carrito> findByUsuario(Usuario usuario);

    @Query("""
            SELECT c FROM Carrito c
            LEFT JOIN FETCH c.items i
            LEFT JOIN FETCH i.producto
            WHERE c.usuario.id = :usuarioId
            """)
    Optional<Carrito> findByUsuarioIdWithItems(Integer usuarioId);
}
