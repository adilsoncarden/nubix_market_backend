package com.nubix.market.module.category.service;

import com.nubix.market.module.category.dto.CategoriaRequest;
import com.nubix.market.module.category.model.Categoria;
import com.nubix.market.module.category.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> obtenerTodas() {
        return categoriaRepository.findAll();
    }

    public Optional<Categoria> obtenerPorId(Integer id) {
        return categoriaRepository.findById(id);
    }

    public Categoria guardar(CategoriaRequest request) {
        if (categoriaRepository.existsByNombre(request.getNombre())) {
            throw new RuntimeException("El nombre de la categoria ya está en uso");
        }
        Categoria categoria = new Categoria();
        categoria.setNombre(request.getNombre());
        return categoriaRepository.save(categoria);
    }

    public Categoria actualizar(Integer id, CategoriaRequest detalles) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));

        if (!categoria.getNombre().equals(detalles.getNombre()) &&
                categoriaRepository.existsByNombre(detalles.getNombre())) {
            throw new RuntimeException("El nuevo nombre de categoria ya esta usada");
        }

        categoria.setNombre(detalles.getNombre());
        return categoriaRepository.save(categoria);
    }

    public void eliminar(Integer id) {
        if (!categoriaRepository.existsById(id)) {
            throw new RuntimeException("Categoría no encontrada");
        }
        categoriaRepository.deleteById(id);
    }
}
