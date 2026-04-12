package com.nubix.market.repositories;

import com.nubix.market.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByUsername(String username);
    //Verificar si el email ya existe
    boolean existsByEmail(String email);
    //Verificar si el username ya existe
    boolean existsByUsername(String username);
}
