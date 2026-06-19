package com.nubix.market.module.favorite.controller;

import com.nubix.market.module.favorite.service.FavoritoService;
import com.nubix.market.module.product.model.Producto;
import com.nubix.market.module.user.model.Usuario;
import com.nubix.market.module.user.repository.UsuarioRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


/**
 * Controlador REST que gestiona la lista de deseos o "favoritos" de los clientes.
 * Proporciona endpoints para que un usuario autenticado pueda ver sus productos guardados,
 * añadir nuevos o eliminarlos.
 */
@RestController
@RequestMapping("/api/favoritos")
public class FavoritoController {

    @Autowired
    private FavoritoService favoritoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Recupera la lista completa de productos que el usuario actual ha marcado como favoritos.
     *
     * @return Respuesta HTTP 200 (OK) con una lista de objetos Producto, o 400 en caso de error.
     */
    @GetMapping
    public ResponseEntity<?> listar() {
        try {
            Usuario u = obtenerUsuarioActual();
            List<Producto> productos = favoritoService.listarFavoritos(u.getId());
            return ResponseEntity.ok(productos);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Alterna el estado de favorito de un producto.
     * Si el producto ya es favorito, lo elimina de la lista. Si no lo es, lo añade.
     * Esto permite al frontend usar un único botón de "corazón" sin preocuparse por el estado previo.
     *
     * @param productoId El identificador del producto a alternar.
     * @return Respuesta HTTP 200 con un booleano: true si se añadió a favoritos, false si se eliminó.
     */
    @PostMapping("/{productoId}/toggle")
    public ResponseEntity<?> toggle(@PathVariable Integer productoId) {
        try {
            Usuario u = obtenerUsuarioActual();
            boolean isFavorite = favoritoService.toggleFavorito(u, productoId);
            return ResponseEntity.ok(isFavorite);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Elimina explícitamente un producto de la lista de favoritos del usuario.
     *
     * @param productoId El identificador del producto a remover.
     * @return Respuesta HTTP 200 (OK) sin contenido si la operación fue exitosa.
     */
    @DeleteMapping("/{productoId}")
    public ResponseEntity<?> eliminar(@PathVariable Integer productoId) {
        try {
            Usuario u = obtenerUsuarioActual();
            favoritoService.eliminarFavorito(u.getId(), productoId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Método auxiliar que extrae al usuario que está haciendo la petición 
     * leyendo su token de seguridad (JWT).
     *
     * @return El objeto Usuario completo de la base de datos.
     * @throws RuntimeException Si no hay una sesión activa o el usuario no existe.
     */
    private Usuario obtenerUsuarioActual() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no autenticado"));
    }
}
