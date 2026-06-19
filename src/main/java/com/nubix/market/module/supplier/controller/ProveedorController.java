package com.nubix.market.module.supplier.controller;

import com.nubix.market.module.supplier.dto.ProveedorRequest;
import com.nubix.market.module.supplier.dto.ProveedorResponse;
import com.nubix.market.module.supplier.model.Proveedor;
import com.nubix.market.module.supplier.service.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST diseñado para gestionar el directorio de proveedores de la empresa.
 * Expone operaciones CRUD exclusivamente para el panel de administración.
 */
@RestController
@RequestMapping("/api/admin")
public class ProveedorController {
    @Autowired
    private ProveedorService proveedorService;

    /**
     * Mapeador manual interno para transformar entidades de BD en DTOs de respuesta.
     */
    private ProveedorResponse mapToResponse(Proveedor proveedor) {
        return new ProveedorResponse(
                proveedor.getId(),
                proveedor.getRuc(),
                proveedor.getNombre(),
                proveedor.getTelefono(),
                proveedor.getEmail());
    }

    /**
     * Obtiene el listado completo de los proveedores registrados en el sistema.
     */
    @GetMapping("/proveedores")
    public ResponseEntity<List<ProveedorResponse>> obtenerTodos() {
        List<Proveedor> proveedores = proveedorService.obtenerTodos();
        List<ProveedorResponse> response = proveedores.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    /**
     * Registra un nuevo proveedor en la base de datos tras validar sus datos fiscales.
     */
    @PostMapping("/proveedores/create")
    public ResponseEntity<?> crearProveedor(@RequestBody ProveedorRequest request) {
        Proveedor proveedor = proveedorService.guardar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponse(proveedor));
    }

    /**
     * Busca los detalles específicos de un proveedor utilizando su ID.
     */
    @GetMapping("/proveedores/{id}")
    public ResponseEntity<?> obtenerProveedorPorId(@PathVariable Integer id) {
        return proveedorService.obtenerPorId(id)
                .map(this::mapToResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Actualiza la información de contacto o el nombre comercial de un proveedor existente.
     */
    @PostMapping("/proveedores/{id}/update")
    public ResponseEntity<?> actualizarProveedor(@PathVariable Integer id, @RequestBody ProveedorRequest request) {
        try {
            Proveedor actualizado = proveedorService.actualizar(id, request);
            return ResponseEntity.ok(mapToResponse(actualizado));
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Proveedor no encontrado")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Elimina a un proveedor del registro del sistema.
     */
    @DeleteMapping("/proveedores/{id}/delete")
    public ResponseEntity<?> eliminarProveedor(@PathVariable Integer id) {
        try {
            proveedorService.eliminar(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Proveedor no encontrado")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
