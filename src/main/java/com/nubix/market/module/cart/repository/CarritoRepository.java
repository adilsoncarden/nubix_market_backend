package com.nubix.market.module.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.nubix.market.module.cart.model.Carrito;
import com.nubix.market.module.user.model.Usuario;
import java.util.Optional;

/**
 * Repositorio de Spring Data JPA para la entidad Carrito.
 * Incluye consultas personalizadas para optimizar la carga de los carritos y sus productos relacionados.
 */
@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Integer> {

    /**
     * Busca el carrito asociado a la entidad completa de un usuario.
     *
     * @param usuario El objeto usuario dueño del carrito.
     * @return El carrito opcional si existe en BD.
     */
    Optional<Carrito> findByUsuario(Usuario usuario);

    /**
     * Consulta optimizada mediante JPQL y FETCH JOIN.
     * Recupera en una sola llamada a la base de datos el carrito, todos sus ítems, 
     * los detalles de cada producto y sus categorías.
     * Esto previene el problema de N+1 consultas (LazyInitializationException) al enviar el carrito al frontend.
     *
     * @param usuarioId El ID numérico del usuario.
     * @return El carrito opcional completamente poblado de datos.
     */
    @Query("""
            SELECT DISTINCT c FROM Carrito c
            LEFT JOIN FETCH c.items i
            LEFT JOIN FETCH i.producto p
            LEFT JOIN FETCH p.categoria
            WHERE c.usuario.id = :usuarioId
            """)
    Optional<Carrito> findByUsuarioIdWithItems(Integer usuarioId);
}
