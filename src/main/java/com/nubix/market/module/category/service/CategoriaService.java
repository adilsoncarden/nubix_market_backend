package com.nubix.market.module.category.service;

import com.nubix.market.module.category.dto.CategoriaRequest;
import com.nubix.market.module.category.model.Categoria;
import com.nubix.market.module.category.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Servicio de negocio para categorías de productos.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    /**
     * Obtiene todas las categorías.
     * @return resultado de la operación
     */
    public List<Categoria> obtenerTodas() {
        return categoriaRepository.findAll();
    }

    /**
     * Obtiene un recurso por identificador.
     * @param id Identificador único.
     * @return resultado de la operación
     */
    public Optional<Categoria> obtenerPorId(Integer id) {
        return categoriaRepository.findById(id);
    }

    /**
     * Persiste un nuevo recurso.
     * @param request valor del parámetro
     * @return resultado de la operación
     */
    public Categoria guardar(CategoriaRequest request) {
        if (categoriaRepository.existsByNombre(request.getNombre())) {
            throw new RuntimeException("El nombre de la categoria ya está en uso");
        }
        Categoria categoria = new Categoria();
        categoria.setNombre(request.getNombre());
        categoria.setDescripcion(request.getDescripcion());
        return categoriaRepository.save(categoria);
    }

    /**
     * Actualiza un recurso existente.
     * @param id Identificador único.
     * @param detalles valor del parámetro
     * @return resultado de la operación
     */
    public Categoria actualizar(Integer id, CategoriaRequest detalles) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));

        if (!categoria.getNombre().equals(detalles.getNombre()) &&
                categoriaRepository.existsByNombre(detalles.getNombre())) {
            throw new RuntimeException("El nuevo nombre de categoria ya esta usada");
        }

        categoria.setNombre(detalles.getNombre());
        categoria.setDescripcion(detalles.getDescripcion());
        return categoriaRepository.save(categoria);
    }

    /**
     * Elimina un recurso.
     * @param id Identificador único.
     */
    public void eliminar(Integer id) {
        if (!categoriaRepository.existsById(id)) {
            throw new RuntimeException("Categoría no encontrada");
        }
        categoriaRepository.deleteById(id);
    }
}
