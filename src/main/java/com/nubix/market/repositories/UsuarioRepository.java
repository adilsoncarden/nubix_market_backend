package com.nubix.market.repositories;

import com.nubix.market.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByUsername(String username);

    Optional<Usuario> findByEmail(String email);

    // Verificar si el email ya existe
    boolean existsByEmail(String email);

    // Verificar si el username ya exista
    boolean existsByUsername(String username);


    List<Usuario> findByRolNombre(String nombreRol);

    List<Usuario> findByRol_NombreIn(List<String> nombresRol);

    java.util.Optional<Usuario> findFirstByRol_Nombre(String nombre);
}
