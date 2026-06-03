package com.nubix.market.module.user.repository;

import com.nubix.market.module.user.model.Permiso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface PermisoRepository extends JpaRepository<Permiso, Integer> {

    Optional<Permiso> findByNombre(String nombre);

    List<Permiso> findAllByOrderByModuloAscNombreAsc();

    List<Permiso> findByModuloOrderByNombreAsc(String modulo);

    @Query("SELECT DISTINCT p.modulo FROM Permiso p ORDER BY p.modulo ASC")
    List<String> findDistinctModulos();
}
