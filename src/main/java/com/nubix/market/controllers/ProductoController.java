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
                                        producto.getNombre(), producto.getDescripcion(), 
                                        producto.getPrecioCompra(), producto.getPrecioVenta(), 
                                        producto.getStock(), producto.getCategoria().getNombre());
        
        List<ProductoResponse.ImagenDTO> imagenes = producto.getImagenes().stream()
                .map(img -> new ProductoResponse.ImagenDTO(img.getId(), img.getUrl()))
                .collect(Collectors.toList());
        response.setImagenes(imagenes);

        return response;
    }

    // GET Listar productos
    @GetMapping("/productos")
    public ResponseEntity<List<ProductoResponse>> index() {
        List<ProductoResponse> productos = productoService.obtenerTodos().stream()
                .map(this::mapToProductoResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productos);
    }

    // POST Crear producto
    @PostMapping("/productos/create")
    public ResponseEntity<?> create(@RequestBody ProductoRequest request) {
        try {
            Producto producto = productoService.guardar(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(mapToProductoResponse(producto));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // GET Obtener producto por ID
    @GetMapping("/productos/{id}")
    public ResponseEntity<ProductoResponse> show(@PathVariable Integer id) {
        return productoService.obtenerPorId(id)
                .map(producto -> ResponseEntity.ok(mapToProductoResponse(producto)))
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT Actualizar producto
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

    // DELETE Eliminar producto
    @DeleteMapping("/productos/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            productoService.eliminar(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
                return ResponseEntity.notFound().build();
        }
    }

    // POST Agregar imagen a producto
    @PostMapping("/productos/{id}/upload_imagene")
    public ResponseEntity<?> uploadImage(@PathVariable Integer id, @RequestParam("file") MultipartFile file) {
        try {
            ProductoImagen imagen = productoService.subirImagen(id, file);
            return ResponseEntity.ok(imagen);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // PUT Actualizar imagen de producto
    @PutMapping("/producto/imagen/{imagenId}/update_imagen")
    public ResponseEntity<?> updateImage(@PathVariable Integer imagenId, @RequestParam("file") MultipartFile file) {
        try {
            ProductoImagen imagen = productoService.actualizarImagen(imagenId, file);
            return ResponseEntity.ok(imagen);
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Imagen no encontrada")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DELETE Eliminar imagen de producto
    @DeleteMapping("/producto/imagen/{imagenId}/delete_imagen")
    public ResponseEntity<?> deleteImage(@PathVariable Integer imagenId) {
        try {
            productoService.eliminarImagen(imagenId);
            return ResponseEntity.ok("Imagen eliminada exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
