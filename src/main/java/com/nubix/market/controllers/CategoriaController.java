package com.nubix.market.controllers;

import com.nubix.market.dto.CategoriaRequest;
import com.nubix.market.dto.CategoriaResponse;
import com.nubix.market.entities.Categoria;
import com.nubix.market.services.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class CategoriaController {
    @Autowired
    private CategoriaService categoriaService;

    private CategoriaResponse mapToResponse(Categoria categoria) {
        return new CategoriaResponse(categoria.getId(),
                categoria.getNombre());
    }

    // GET: Listar todas las categorías
    @GetMapping("/categorias")
    public ResponseEntity<List<CategoriaResponse>> index() {
        List<CategoriaResponse> categorias = categoriaService.obtenerTodas()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(categorias);
    }

    // POST: Crear una nueva categoría
    @PostMapping("/categorias/create")
    public ResponseEntity<?> create(@RequestBody CategoriaRequest request) {
        try {
            Categoria categoria = categoriaService.guardar(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponse(categoria));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // GET: Obtener una categoría por ID
    @GetMapping("/categorias/{id}")
    public ResponseEntity<CategoriaResponse> show(@PathVariable Integer id) {
        return categoriaService.obtenerPorId(id)
                .map(categoria -> ResponseEntity.ok(mapToResponse(categoria)))
                .orElse(ResponseEntity.notFound().build());
    }

    // POST: Actualizar una categoría existente
    @PostMapping("/categorias/{id}/update")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody CategoriaRequest request) {
        try {
            Categoria categoria = categoriaService.actualizar(id, request);
            return ResponseEntity.ok(mapToResponse(categoria));
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Categoria no encontrada")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DELETE: Eliminar una categoría por ID
    @DeleteMapping("/categorias/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            categoriaService.eliminar(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
