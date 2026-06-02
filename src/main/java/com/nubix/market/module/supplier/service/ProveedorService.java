package com.nubix.market.module.supplier.service;

import com.nubix.market.module.supplier.dto.ProveedorRequest;
import com.nubix.market.module.supplier.model.Proveedor;
import com.nubix.market.module.supplier.repository.ProveedorRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProveedorService {
    @Autowired
    private ProveedorRepository proveedorRepository;

    public List<Proveedor> obtenerTodos() {
        return proveedorRepository.findAll();
    }

    public Optional<Proveedor> obtenerPorId(Integer id) {
        return proveedorRepository.findById(id);
    }

    public Proveedor guardar(ProveedorRequest request) {
        validarProveedor(request);
        if (proveedorRepository.existsByRuc(request.getRuc())) {
            throw new RuntimeException("El RUC del proveedor ya existe");
        }

        Proveedor proveedor = new Proveedor();
        proveedor.setRuc(request.getRuc());
        proveedor.setNombre(request.getNombre());
        proveedor.setTelefono(request.getTelefono());
        proveedor.setEmail(request.getEmail());

        return proveedorRepository.save(proveedor);
    }

    public Proveedor actualizar(Integer id, ProveedorRequest detalles) {
        validarProveedor(detalles);
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        if (proveedorRepository.existsByRucAndIdNot(detalles.getRuc(), id)) {
            throw new RuntimeException("El RUC del proveedor ya existe");
        }

        proveedor.setRuc(detalles.getRuc());
        proveedor.setNombre(detalles.getNombre());
        proveedor.setTelefono(detalles.getTelefono());
        proveedor.setEmail(detalles.getEmail());

        return proveedorRepository.save(proveedor);
    }

    public void eliminar(Integer id) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        proveedorRepository.delete(proveedor);
    }

    private void validarProveedor(ProveedorRequest request) {
        if (request.getRuc() != null) {
            request.setRuc(request.getRuc().trim());
        }
        if (request.getTelefono() != null) {
            request.setTelefono(request.getTelefono().trim());
        }
        if (request.getNombre() != null) {
            request.setNombre(request.getNombre().trim());
        }
        if (request.getEmail() != null) {
            request.setEmail(request.getEmail().trim());
        }
        if (request.getRuc() == null || !request.getRuc().matches("\\d{11}")) {
            throw new RuntimeException("El RUC debe tener 11 dígitos numéricos");
        }
        if (request.getTelefono() == null || !request.getTelefono().matches("\\d{9}")) {
            throw new RuntimeException("El teléfono debe tener 9 dígitos numéricos");
        }
        if (request.getNombre() == null || request.getNombre().isBlank()) {
            throw new RuntimeException("El nombre del proveedor es obligatorio");
        }
        if (request.getEmail() == null || !request.getEmail().contains("@")) {
            throw new RuntimeException("El email no es válido");
        }
    }
}