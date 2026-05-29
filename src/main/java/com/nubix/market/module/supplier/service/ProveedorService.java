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
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        if (!proveedor.getRuc().equals(detalles.getRuc()) && proveedorRepository.existsByRuc(detalles.getRuc())) {
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
}