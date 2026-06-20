package com.nubix.market.module.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.nubix.market.module.user.model.Usuario;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio principal de Spring Data JPA para la gestión de las cuentas de Usuario.
 * Contiene consultas cruciales para la autenticación (Login) y la verificación de 
 * duplicados durante el registro.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    /** Busca a un usuario por su nombre de cuenta exacto. */
    Optional<Usuario> findByUsername(String username);

    /** Busca a un usuario por su correo electrónico. */
    Optional<Usuario> findByEmail(String email);

    /**
     * Recupera el usuario y fuerza la carga inmediata de su Rol.
     * Esta consulta es vital durante el proceso de autenticación de Spring Security 
     * (UserDetailsService) para saber qué autoridades tiene la persona que está ingresando.
     */
    @Query("SELECT DISTINCT u FROM Usuario u JOIN FETCH u.rol WHERE LOWER(u.username) = LOWER(:username)")
    Optional<Usuario> findByUsernameWithRol(@Param("username") String username);

    /**
     * Similar al método anterior, pero busca por correo electrónico, garantizando 
     * la extracción del Rol en la misma transacción SQL.
     */
    @Query("SELECT DISTINCT u FROM Usuario u JOIN FETCH u.rol WHERE LOWER(u.email) = LOWER(:email)")
    Optional<Usuario> findByEmailWithRol(@Param("email") String email);

    /** Verifica si un correo electrónico ya está registrado. Fundamental en el Sign-Up. */
    boolean existsByEmail(String email);

    /** Verifica si un nombre de usuario ya está ocupado en la plataforma. */
    // Verificar si el username ya exista
    boolean existsByUsername(String username);

    /** Filtra usuarios que pertenecen a un único rol específico (ej. "CLIENTE"). */
    List<Usuario> findByRol_Nombre(String nombreRol);

    /** Filtra usuarios que coinciden con una lista de roles (ej. ["ADMIN", "EMPLEADO"]). */
    List<Usuario> findByRol_NombreIn(List<String> nombresRol);

    /** Excluye de la búsqueda a los usuarios de un rol particular (ej. traer a todos menos a los "CLIENTE"). */
    List<Usuario> findByRol_NombreNot(String nombreRol);

    /** * Busca al primer usuario que encuentre con el rol especificado.
     * Útil para asignar tareas automáticamente (ej. asignar el primer "ADMIN" encontrado como remitente del sistema).
     */
    java.util.Optional<Usuario> findFirstByRol_Nombre(String nombre);

    /** Cuenta cuántas cuentas de usuario están actualmente asignadas a un Rol por su ID. */
    long countByRol_Id(Integer rolId);
}
