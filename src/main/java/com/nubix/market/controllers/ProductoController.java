package com.nubix.market.controllers;

import com.nubix.market.dto.ProductoRequest;
import com.nubix.market.dto.ProductoResponse;
import com.nubix.market.entities.Producto;
import com.nubix.market.entities.ProductoImagen;
import com.nubix.market.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class ProductoController {
    @Autowired
    private ProductoService productoService;

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
    public ResponseEntity<List<ProductoResponse>> index() {
        List<ProductoResponse> productos = productoService.obtenerTodos().stream()
                .map(this::mapToProductoResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productos);
    }

    @PostMapping("/productos/create")
    public ResponseEntity<?> create(@RequestBody ProductoRequest request) {
        try {
            Producto producto = productoService.guardar(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(mapToProductoResponse(producto));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/productos/{id}")
    public ResponseEntity<ProductoResponse> show(@PathVariable Integer id) {
        return productoService.obtenerPorId(id)
                .map(producto -> ResponseEntity.ok(mapToProductoResponse(producto)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/productos/update/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody ProductoRequest request) {
        try {
            Producto producto = productoService.actualizar(id, request);
            return ResponseEntity.ok(mapToProductoResponse(producto));
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/productos/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            productoService.eliminar(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/productos/{id}/imagen")
    public ResponseEntity<?> uploadProductImage(
            @PathVariable Integer id,
            @RequestParam("archivo") MultipartFile archivo) {
        try {
            Producto producto = productoService.subirImagenProducto(id, archivo);
            return ResponseEntity.ok(mapToProductoResponse(producto));
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/productos/{id}/imagen")
    public ResponseEntity<?> deleteProductImage(@PathVariable Integer id) {
        try {
            Producto producto = productoService.eliminarImagenProducto(id);
            return ResponseEntity.ok(mapToProductoResponse(producto));
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
