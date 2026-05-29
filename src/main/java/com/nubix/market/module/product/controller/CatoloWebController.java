package com.nubix.market.module.product.controller;

import com.nubix.market.module.category.dto.CategoriaResponse;
import com.nubix.market.module.category.service.CategoriaService;
import com.nubix.market.module.media.model.ProductoImagen;
import com.nubix.market.module.product.dto.ProductoResponse;
import com.nubix.market.module.product.model.Producto;
import com.nubix.market.module.product.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/catalogo")
@CrossOrigin(origins = "*")
public class CatoloWebController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    private ProductoResponse mapToProductoResponse(Producto producto) {
        ProductoResponse response = new ProductoResponse(
                producto.getId(), producto.getCodigo(),
                producto.getNombre(), producto.getPrecioCompra(), producto.getPrecioVenta(),
                producto.getStock(), producto.getCategoria().getNombre());

        ProductoImagen imagen = producto.getImagen();
        if (imagen != null) {
            response.setImagen(new ProductoResponse.ImagenDTO(imagen.getId(), imagen.getArchivo()));
        }
        return response;
    }

    @GetMapping("/productos")
    public ResponseEntity<List<ProductoResponse>> listarProductos() {
        List<ProductoResponse> productos = productoService.obtenerTodos().stream()
                .map(this::mapToProductoResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/categorias")
    public ResponseEntity<List<CategoriaResponse>> listarCategorias() {
        List<CategoriaResponse> categorias = categoriaService.obtenerTodas().stream()
                .map(c -> new CategoriaResponse(c.getId(), c.getNombre()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(categorias);
    }
}
