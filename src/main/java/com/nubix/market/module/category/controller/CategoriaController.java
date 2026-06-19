package com.nubix.market.module.category.controller;

import com.nubix.market.module.category.dto.CategoriaRequest;
import com.nubix.market.module.category.dto.CategoriaResponse;
import com.nubix.market.module.category.model.Categoria;
import com.nubix.market.module.category.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST encargado de la gestión de categorías en el panel administrativo.
 * Proporciona los endpoints (CRUD) necesarios para que los administradores puedan 
 * listar, crear, visualizar, editar y eliminar las categorías del catálogo de productos.
 */
@RestController
@RequestMapping("/api/admin")
public class CategoriaController {
    @Autowired
    private CategoriaService categoriaService;

    /**
     * Método auxiliar privado para transformar una entidad de base de datos (Categoria) 
     * en un objeto de transferencia de datos (CategoriaResponse) seguro para el frontend.
     *
     * @param categoria La entidad obtenida de la base de datos.
     * @return El DTO formateado para la respuesta HTTP.
     */
    private CategoriaResponse mapToResponse(Categoria categoria) {
        return new CategoriaResponse(categoria.getId(),
                categoria.getNombre(), categoria.getDescripcion());
    }

    /**
     * Obtiene y devuelve la lista completa de todas las categorías registradas en el sistema.
     *
     * @return Respuesta HTTP 200 (OK) con una lista de objetos CategoriaResponse.
     */
    @GetMapping("/categorias")
    public ResponseEntity<List<CategoriaResponse>> index() {
        List<CategoriaResponse> categorias = categoriaService.obtenerTodas()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(categorias);
    }

/**
     * Crea una nueva categoría en el catálogo a partir de los datos enviados por el cliente.
     *
     * @param request DTO que contiene el nombre y la descripción de la nueva categoría.
     * @return Respuesta HTTP 201 (Created) con los datos de la categoría creada, 
     * o HTTP 400 (Bad Request) si falla alguna validación o regla de negocio.
     */
    @PostMapping("/categorias/create")
    public ResponseEntity<?> create(@RequestBody CategoriaRequest request) {
        try {
            Categoria categoria = categoriaService.guardar(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponse(categoria));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Busca y devuelve los detalles de una categoría específica utilizando su identificador único.
     *
     * @param id El identificador único (ID) de la categoría solicitada.
     * @return Respuesta HTTP 200 (OK) con el detalle de la categoría, o HTTP 404 (Not Found) si no existe.
     */
    @GetMapping("/categorias/{id}")
    public ResponseEntity<CategoriaResponse> show(@PathVariable Integer id) {
        return categoriaService.obtenerPorId(id)
                .map(categoria -> ResponseEntity.ok(mapToResponse(categoria)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Actualiza la información (nombre o descripción) de una categoría existente.
     *
     * @param id      El identificador único de la categoría a modificar.
     * @param request DTO con los nuevos datos que reemplazarán a los actuales.
     * @return Respuesta HTTP 200 (OK) con la categoría actualizada, HTTP 404 si no se encuentra, 
     * o HTTP 400 si ocurre un error de validación.
     */
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

    /**
     * Elimina permanentemente una categoría del sistema.
     *
     * @param id El identificador único de la categoría a eliminar.
     * @return Respuesta HTTP 200 (OK) sin contenido si la eliminación es exitosa, 
     * o HTTP 404 (Not Found) si la categoría no existe o no puede ser eliminada.
     */
    @DeleteMapping("/categorias/{id}/delete")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            categoriaService.eliminar(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
