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
 * Controlador REST público (sin autenticación obligatoria) diseñado para alimentar 
 * la vista de la tienda virtual (vitrina de productos).
 * Devuelve únicamente la información segura y necesaria para que los clientes 
 * puedan navegar por el catálogo.
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
     * Lista todos los productos disponibles en la tienda.
     * Utiliza un DTO específico (ProductoPublicResponse) para ocultar datos sensibles 
     * como el costo interno o márgenes de ganancia.
     *
     * @return Respuesta HTTP 200 (OK) con la lista de productos públicos.
     */
    @GetMapping("/productos")
    public ResponseEntity<List<ProductoPublicResponse>> listarProductos() {
        List<ProductoPublicResponse> productos = productoService.obtenerTodos().stream()
                .map(productoMapper::toPublicResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productos);
    }

    /**
     * Devuelve el detalle completo y público de un solo producto.
     * Ideal para la página de "Detalle de Producto" cuando el cliente hace clic en una tarjeta.
     *
     * @param id El identificador único del producto solicitado.
     * @return Respuesta HTTP 200 con el DTO público, o 404 (Not Found) si el producto no existe.
     */
    @GetMapping("/productos/{id}")
    public ResponseEntity<ProductoPublicResponse> obtenerProducto(@PathVariable Integer id) {
        return productoService.obtenerPorId(id)
                .map(productoMapper::toPublicResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todas las categorías para construir los menús de navegación o filtros 
     * en la parte lateral de la tienda web.
     *
     * @return Respuesta HTTP 200 (OK) con la lista de categorías.
     */
    @GetMapping("/categorias")
    public ResponseEntity<List<CategoriaResponse>> listarCategorias() {
        List<CategoriaResponse> categorias = categoriaService.obtenerTodas().stream()
                .map(c -> new CategoriaResponse(c.getId(), c.getNombre(), c.getDescripcion()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(categorias);
    }
}
