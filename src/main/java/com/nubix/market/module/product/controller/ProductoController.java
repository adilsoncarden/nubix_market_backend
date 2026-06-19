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

/**
 * Controlador REST privado y protegido encargado de gestionar el inventario de productos.
 * Utilizado de manera exclusiva por los administradores a través del panel de control.
 * Expone un CRUD completo para mantener el catálogo actualizado.
 */
@RestController
@RequestMapping("/api/admin")
public class ProductoController {

    private final ProductoService productoService;
    private final ProductoMapper productoMapper;

    public ProductoController(ProductoService productoService, ProductoMapper productoMapper) {
        this.productoService = productoService;
        this.productoMapper = productoMapper;
    }

    /**
     * Lista todos los productos en el sistema, incluyendo datos confidenciales de administración.
     *
     * @return Respuesta HTTP 200 con la lista completa de DTOs administrativos.
     */
    @GetMapping("/productos")
    public ResponseEntity<List<ProductoResponse>> index() {
        List<ProductoResponse> productos = productoService.obtenerTodos().stream()
                .map(productoMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productos);
    }

    /**
     * Da de alta un nuevo producto en el catálogo.
     *
     * @param request DTO con los detalles del nuevo producto (nombre, precio, stock, etc.).
     * @return Respuesta HTTP 201 (Created) si el guardado fue exitoso, o 400 en caso de error.
     */
    @PostMapping("/productos/create")
    public ResponseEntity<?> create(@RequestBody ProductoRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(productoMapper.toResponse(productoService.guardar(request)));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Consulta el detalle administrativo de un producto específico para editarlo.
     *
     * @param id Identificador único del producto.
     * @return Respuesta HTTP 200 con el DTO administrativo, o 404 si no se encuentra.
     */
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

    /**
     * Modifica los datos (precio, nombre, categoría, stock) de un producto existente.
     *
     * @param id      El ID del producto a modificar.
     * @param request DTO con los nuevos datos.
     * @return Respuesta HTTP 200 con el producto modificado.
     */
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

    /**
     * Elimina permanentemente un producto de la base de datos.
     *
     * @param id El ID del producto a eliminar.
     * @return Respuesta HTTP 200 si la operación fue exitosa.
     */
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
