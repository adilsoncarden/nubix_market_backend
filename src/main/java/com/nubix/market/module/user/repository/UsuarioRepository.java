package com.nubix.market.module.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.nubix.market.module.user.model.Usuario;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad {@link Usuario}.
 * <p>
 * Proporciona consultas de acceso por credenciales, existencia de username/email,
 * filtrado por rol y conteo de usuarios por rol.
 * </p>
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    /**
     * Busca un usuario por nombre de usuario exacto.
     *
     * @param username nombre de usuario
     * @return {@link Optional} con el usuario si existe
     */
    Optional<Usuario> findByUsername(String username);

    /**
     * Busca un usuario por correo electrónico exacto.
     *
     * @param email correo electrónico
     * @return {@link Optional} con el usuario si existe
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Busca un usuario por nombre de usuario (insensible a mayúsculas) cargando su rol.
     *
     * @param username nombre de usuario
     * @return {@link Optional} con el usuario y su rol si existe
     */
    @Query("SELECT DISTINCT u FROM Usuario u JOIN FETCH u.rol WHERE LOWER(u.username) = LOWER(:username)")
    Optional<Usuario> findByUsernameWithRol(@Param("username") String username);

    /**
     * Busca un usuario por correo (insensible a mayúsculas) cargando su rol.
     *
     * @param email correo electrónico
     * @return {@link Optional} con el usuario y su rol si existe
     */
    @Query("SELECT DISTINCT u FROM Usuario u JOIN FETCH u.rol WHERE LOWER(u.email) = LOWER(:email)")
    Optional<Usuario> findByEmailWithRol(@Param("email") String email);

    // Verificar si el email ya existe

    /**
     * Indica si ya existe un usuario con el correo indicado.
     *
     * @param email correo electrónico a verificar
     * @return {@code true} si el email ya está registrado
     */
    boolean existsByEmail(String email);

    // Verificar si el username ya exista

    /**
     * Indica si ya existe un usuario con el nombre de usuario indicado.
     *
     * @param username nombre de usuario a verificar
     * @return {@code true} si el username ya está registrado
     */
    boolean existsByUsername(String username);

    /**
     * Lista usuarios cuyo rol tiene el nombre indicado.
     *
     * @param nombreRol nombre del rol (p. ej. {@code CLIENTE})
     * @return lista de usuarios con ese rol
     */
    List<Usuario> findByRol_Nombre(String nombreRol);

    /**
     * Lista usuarios cuyo rol está entre los nombres indicados.
     *
     * @param nombresRol lista de nombres de rol
     * @return lista de usuarios con alguno de esos roles
     */
    List<Usuario> findByRol_NombreIn(List<String> nombresRol);

    /**
     * Lista usuarios cuyo rol no tiene el nombre indicado.
     *
     * @param nombreRol nombre del rol a excluir
     * @return lista de usuarios sin ese rol
     */
    List<Usuario> findByRol_NombreNot(String nombreRol);

    /**
     * Obtiene el primer usuario con el rol indicado.
     *
     * @param nombre nombre del rol
     * @return {@link Optional} con el primer usuario encontrado
     */
    java.util.Optional<Usuario> findFirstByRol_Nombre(String nombre);

    /**
     * Cuenta cuántos usuarios tienen asignado el rol indicado.
     *
     * @param rolId identificador del rol
     * @return número de usuarios con ese rol
     */
    long countByRol_Id(Integer rolId);
}
