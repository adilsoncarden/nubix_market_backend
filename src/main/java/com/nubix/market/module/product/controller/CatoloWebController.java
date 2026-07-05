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

/**
 * Controlador REST del catálogo público web de productos y categorías.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@RestController
@RequestMapping("/api/catalogo")
public class CatoloWebController {

    @Autowired
    private ProductoService productoService;
    @Autowired
    private CategoriaService categoriaService;
    @Autowired
    private ProductoMapper productoMapper;

    /**
     * Lista productos del catálogo público.
     * @return resultado de la operación
     */
    @GetMapping("/productos")
    public ResponseEntity<List<ProductoPublicResponse>> listarProductos() {
        List<ProductoPublicResponse> productos = productoService.obtenerTodos().stream()
                .map(productoMapper::toPublicResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productos);
    }

    /**
     * Obtiene detalle público de un producto.
     * @param id Identificador único.
     * @return resultado de la operación
     */
    @GetMapping("/productos/{id}")
    public ResponseEntity<ProductoPublicResponse> obtenerProducto(@PathVariable Integer id) {
        return productoService.obtenerPorId(id)
                .map(productoMapper::toPublicResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista categorías del catálogo público.
     * @return resultado de la operación
     */
    @GetMapping("/categorias")
    public ResponseEntity<List<CategoriaResponse>> listarCategorias() {
        List<CategoriaResponse> categorias = categoriaService.obtenerTodas().stream()
                .map(c -> new CategoriaResponse(c.getId(), c.getNombre(), c.getDescripcion()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(categorias);
    }
}
