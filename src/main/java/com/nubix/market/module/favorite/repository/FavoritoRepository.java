package com.nubix.market.module.favorite.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.nubix.market.module.favorite.model.Favorito;

@Repository
public interface FavoritoRepository extends JpaRepository<Favorito, Integer> {
    List<Favorito> findAllByUsuario_Id(Integer usuarioId);

    Optional<Favorito> findByUsuario_IdAndProducto_Id(Integer usuarioId, Integer productoId);

    boolean existsByUsuario_IdAndProducto_Id(Integer usuarioId, Integer productoId);

    void deleteByUsuario_IdAndProducto_Id(Integer usuarioId, Integer productoId);
}
