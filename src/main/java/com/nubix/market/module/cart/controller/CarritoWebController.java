package com.nubix.market.module.cart.controller;

import com.nubix.market.module.cart.dto.CarritoItemRequest;
import com.nubix.market.module.cart.model.Carrito;
import com.nubix.market.module.cart.service.CarritoService;
import com.nubix.market.module.user.model.Usuario;
import com.nubix.market.module.user.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST encargado de gestionar las operaciones del carrito de compras 
 * para los clientes de la tienda web.
 * Todos los endpoints de esta clase requieren que el usuario esté autenticado, 
 * ya que el carrito se asocia dinámicamente a la sesión actual.
 */
@RestController
@RequestMapping("/api/carrito")
public class CarritoWebController {

    @Autowired
    private CarritoService carritoService;
    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Recupera el carrito de compras activo del usuario autenticado, 
     * incluyendo todos sus items y los totales calculados.
     *
     * @return Respuesta HTTP 200 con el objeto Carrito, o 400 si ocurre un error de validación.
     */
    @GetMapping
    public ResponseEntity<?> obtenerCarrito() {
        try {
            return ResponseEntity.ok(carritoService.obtenerCarritoUsuario(obtenerUsuarioActual().getId()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Añade un nuevo producto al carrito del usuario o incrementa su cantidad 
     * si el producto ya se encontraba previamente en el carrito.
     *
     * @param request DTO que contiene el ID del producto y la cantidad a añadir.
     * @return Respuesta HTTP 200 con el carrito actualizado, o 400 en caso de error (ej. falta de stock).
     */
    @PostMapping("/items")
    public ResponseEntity<?> agregarItem(@RequestBody CarritoItemRequest request) {
        try {
            Carrito carrito = carritoService.agregarItem(obtenerUsuarioActual().getId(), request);
            return ResponseEntity.ok(carrito);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Actualiza la cantidad exacta de un producto específico que ya se encuentra en el carrito.
     * Útil cuando el usuario usa los botones de "+" o "-" en la vista del carrito.
     *
     * @param productoId El identificador único del producto a modificar.
     * @param cantidad   La nueva cantidad deseada.
     * @return Respuesta HTTP 200 con el carrito actualizado, o 400 en caso de error.
     */
    @PutMapping("/items/{productoId}")
    public ResponseEntity<?> actualizarItem(
            @PathVariable Integer productoId,
            @RequestParam Integer cantidad) {
        try {
            Carrito carrito = carritoService.actualizarCantidad(
                    obtenerUsuarioActual().getId(), productoId, cantidad);
            return ResponseEntity.ok(carrito);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Elimina por completo un producto específico del carrito del usuario, 
     * independientemente de la cantidad que tuviera.
     *
     * @param productoId El identificador único del producto a remover.
     * @return Respuesta HTTP 200 con el carrito actualizado, o 400 en caso de error.
     */
    @DeleteMapping("/items/{productoId}")
    public ResponseEntity<?> eliminarItem(@PathVariable Integer productoId) {
        try {
            Carrito carrito = carritoService.eliminarItem(obtenerUsuarioActual().getId(), productoId);
            return ResponseEntity.ok(carrito);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Vacía completamente el carrito de compras del usuario, eliminando todos sus items.
     *
     * @return Respuesta HTTP 200 sin cuerpo (OK) si la operación fue exitosa, o 400 en caso de error.
     */
    @DeleteMapping
    public ResponseEntity<?> vaciarCarrito() {
        try {
            carritoService.vaciarCarrito(obtenerUsuarioActual().getId());
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Método auxiliar privado que extrae la identidad del usuario que está realizando 
     * la petición directamente desde el contexto de seguridad (JWT) de Spring.
     *
     * @return La entidad Usuario completa extraída de la base de datos.
     * @throws RuntimeException Si la sesión es inválida o el usuario no existe.
     */
    private Usuario obtenerUsuarioActual() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no autenticado"));
    }
}
