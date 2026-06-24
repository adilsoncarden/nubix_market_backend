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
 * Controlador REST del carrito de compras del cliente web.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@RestController
@RequestMapping("/api/carrito")
public class CarritoWebController {

    @Autowired
    private CarritoService carritoService;
    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Obtiene el carrito del usuario autenticado.
     * @return resultado de la operación
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
     * Agrega un producto al carrito.
     * @param request valor del parámetro
     * @return resultado de la operación
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
     * Actualiza cantidad en carrito.
     * @param productoId valor del parámetro
     * @param cantidad Cantidad de unidades.
     * @return resultado de la operación
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
     * Elimina un producto del carrito.
     * @param productoId valor del parámetro
     * @return resultado de la operación
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
     * Vacía el carrito.
     * @return resultado de la operación
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

    private Usuario obtenerUsuarioActual() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no autenticado"));
    }
}
