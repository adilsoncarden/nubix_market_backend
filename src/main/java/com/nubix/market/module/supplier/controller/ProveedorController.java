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

@RestController
@RequestMapping("/api/admin")
public class ProveedorController {
    @Autowired
    private ProveedorService proveedorService;

    private ProveedorResponse mapToResponse(Proveedor proveedor) {
        return new ProveedorResponse(
                proveedor.getId(),
                proveedor.getRuc(),
                proveedor.getNombre(),
                proveedor.getTelefono(),
                proveedor.getEmail());
    }

    @GetMapping("/proveedores")
    public ResponseEntity<List<ProveedorResponse>> obtenerTodos() {
        List<Proveedor> proveedores = proveedorService.obtenerTodos();
        List<ProveedorResponse> response = proveedores.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/proveedores/create")
    public ResponseEntity<?> crearProveedor(@RequestBody ProveedorRequest request) {
        Proveedor proveedor = proveedorService.guardar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponse(proveedor));
    }

    @GetMapping("/proveedores/{id}")
    public ResponseEntity<?> obtenerProveedorPorId(@PathVariable Integer id) {
        return proveedorService.obtenerPorId(id)
                .map(this::mapToResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

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
