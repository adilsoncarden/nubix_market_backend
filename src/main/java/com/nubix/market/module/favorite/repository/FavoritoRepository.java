package com.nubix.market.module.favorite.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.nubix.market.module.favorite.model.Favorito;

/**
 * Repositorio de Spring Data JPA para la entidad Favorito.
 * Contiene métodos de consulta derivados para buscar, verificar existencia 
 * y eliminar relaciones específicas entre usuarios y productos.
 */
@Repository
public interface FavoritoRepository extends JpaRepository<Favorito, Integer> {

    /** Busca todos los registros de favoritos de un usuario en particular. */
    List<Favorito> findAllByUsuario_Id(Integer usuarioId);

    /** Busca un registro específico de favorito basado en el usuario y el producto. */
    Optional<Favorito> findByUsuario_IdAndProducto_Id(Integer usuarioId, Integer productoId);

    /** Verifica si un usuario ya tiene marcado un producto específico en su lista. */
    boolean existsByUsuario_IdAndProducto_Id(Integer usuarioId, Integer productoId);

    /** Elimina un registro de favorito coincidiendo el usuario y el producto. */
    void deleteByUsuario_IdAndProducto_Id(Integer usuarioId, Integer productoId);
}
