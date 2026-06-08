package com.nubix.market.module.product.controller;

import com.nubix.market.module.category.dto.CategoriaResponse;
import com.nubix.market.module.category.service.CategoriaService;
import com.nubix.market.module.product.dto.ProductoPublicResponse;
import com.nubix.market.module.product.mapper.ProductoMapper;
import com.nubix.market.module.product.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/catalogo")
public class CatoloWebController {

    @Autowired
    private ProductoService productoService;
    @Autowired
    private CategoriaService categoriaService;
    @Autowired
    private ProductoMapper productoMapper;

    @GetMapping("/productos")
    public ResponseEntity<List<ProductoPublicResponse>> listarProductos() {
        List<ProductoPublicResponse> productos = productoService.obtenerTodos().stream()
                .map(productoMapper::toPublicResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/productos/{id}")
    public ResponseEntity<ProductoPublicResponse> obtenerProducto(@PathVariable Integer id) {
        return productoService.obtenerPorId(id)
                .map(productoMapper::toPublicResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/categorias")
    public ResponseEntity<List<CategoriaResponse>> listarCategorias() {
        List<CategoriaResponse> categorias = categoriaService.obtenerTodas().stream()
                .map(c -> new CategoriaResponse(c.getId(), c.getNombre(), c.getDescripcion()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(categorias);
    }
}
