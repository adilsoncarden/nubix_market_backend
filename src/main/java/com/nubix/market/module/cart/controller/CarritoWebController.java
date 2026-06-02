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

@RestController
@RequestMapping("/api/carrito")
public class CarritoWebController {

    @Autowired
    private CarritoService carritoService;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public ResponseEntity<?> obtenerCarrito() {
        try {
            return ResponseEntity.ok(carritoService.obtenerCarritoUsuario(obtenerUsuarioActual().getId()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/items")
    public ResponseEntity<?> agregarItem(@RequestBody CarritoItemRequest request) {
        try {
            Carrito carrito = carritoService.agregarItem(obtenerUsuarioActual().getId(), request);
            return ResponseEntity.ok(carrito);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

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

    @DeleteMapping("/items/{productoId}")
    public ResponseEntity<?> eliminarItem(@PathVariable Integer productoId) {
        try {
            Carrito carrito = carritoService.eliminarItem(obtenerUsuarioActual().getId(), productoId);
            return ResponseEntity.ok(carrito);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

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
