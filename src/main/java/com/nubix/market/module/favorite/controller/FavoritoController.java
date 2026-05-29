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

@RestController
@RequestMapping("/api/favoritos")
@CrossOrigin(origins = "*")
public class FavoritoController {

    @Autowired
    private FavoritoService favoritoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

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

    private Usuario obtenerUsuarioActual() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no autenticado"));
    }
}
