package com.nubix.market.module.product.controller;

import com.nubix.market.module.product.dto.ProductoRequest;
import com.nubix.market.module.product.dto.ProductoResponse;
import com.nubix.market.module.product.mapper.ProductoMapper;
import com.nubix.market.module.product.service.ProductoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class ProductoController {

    private final ProductoService productoService;
    private final ProductoMapper productoMapper;

    public ProductoController(ProductoService productoService, ProductoMapper productoMapper) {
        this.productoService = productoService;
        this.productoMapper = productoMapper;
    }

    @GetMapping("/productos")
    public ResponseEntity<List<ProductoResponse>> index() {
        List<ProductoResponse> productos = productoService.obtenerTodos().stream()
                .map(productoMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productos);
    }

    @PostMapping("/productos/create")
    public ResponseEntity<?> create(@RequestBody ProductoRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(productoMapper.toResponse(productoService.guardar(request)));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/productos/{id}")
    public ResponseEntity<ProductoResponse> show(@PathVariable Integer id) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().build();
        }
        return productoService.obtenerPorId(id)
                .map(productoMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/productos/{id}/update")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody ProductoRequest request) {
        try {
            return ResponseEntity.ok(productoMapper.toResponse(productoService.actualizar(id, request)));
        } catch (RuntimeException e) {
            if (StringUtils.containsIgnoreCase(e.getMessage(), "no encontrado")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/productos/{id}/delete")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            productoService.eliminar(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
}
