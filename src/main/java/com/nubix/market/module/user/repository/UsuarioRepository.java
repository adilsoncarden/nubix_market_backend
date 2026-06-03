package com.nubix.market.module.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.nubix.market.module.user.model.Usuario;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByUsername(String username);

    Optional<Usuario> findByEmail(String email);

    @Query("SELECT DISTINCT u FROM Usuario u JOIN FETCH u.rol WHERE LOWER(u.username) = LOWER(:username)")
    Optional<Usuario> findByUsernameWithRol(@Param("username") String username);

    @Query("SELECT DISTINCT u FROM Usuario u JOIN FETCH u.rol WHERE LOWER(u.email) = LOWER(:email)")
    Optional<Usuario> findByEmailWithRol(@Param("email") String email);

    // Verificar si el email ya existe
    boolean existsByEmail(String email);

    // Verificar si el username ya exista
    boolean existsByUsername(String username);

    List<Usuario> findByRol_Nombre(String nombreRol);

    List<Usuario> findByRol_NombreIn(List<String> nombresRol);

    List<Usuario> findByRol_NombreNot(String nombreRol);

    java.util.Optional<Usuario> findFirstByRol_Nombre(String nombre);

    long countByRol_Id(Integer rolId);
}
