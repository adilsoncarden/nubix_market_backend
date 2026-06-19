package com.nubix.market.module.category.service;

import com.nubix.market.module.category.dto.CategoriaRequest;
import com.nubix.market.module.category.model.Categoria;
import com.nubix.market.module.category.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Servicio encargado de gestionar la lógica de negocio central de las categorías.
 * Aplica validaciones de unicidad (nombres no repetidos) antes de permitir que 
 * los datos lleguen a la base de datos.
 */
@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    /**
     * Recupera todas las categorías registradas en el sistema.
     *
     * @return Una lista con todas las entidades Categoria.
     */
    public List<Categoria> obtenerTodas() {
        return categoriaRepository.findAll();
    }

    /**
     * Busca una categoría específica por su identificador.
     *
     * @param id El identificador de la categoría.
     * @return Un Optional que contiene la categoría si fue encontrada.
     */
    public Optional<Categoria> obtenerPorId(Integer id) {
        return categoriaRepository.findById(id);
    }

    /**
     * Registra una nueva categoría en la base de datos.
     * Valida previamente que el nombre propuesto no esté siendo utilizado por otra categoría.
     *
     * @param request DTO con los datos de la nueva categoría (nombre y descripción).
     * @return La categoría recién guardada con su ID generado.
     * @throws RuntimeException Si el nombre de la categoría ya existe.
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
     * Actualiza los datos de una categoría existente.
     * Verifica que la categoría exista y que, si se cambia el nombre, el nuevo nombre 
     * no colisione con el de otra categoría existente.
     *
     * @param id       El ID de la categoría a actualizar.
     * @param detalles DTO con los nuevos datos de la categoría.
     * @return La categoría actualizada y persistida.
     * @throws RuntimeException Si la categoría no existe o si el nuevo nombre ya está ocupado.
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
     * Elimina permanentemente una categoría de la base de datos.
     *
     * @param id El identificador de la categoría a eliminar.
     * @throws RuntimeException Si se intenta eliminar una categoría que no existe.
     */
    public void eliminar(Integer id) {
        if (!categoriaRepository.existsById(id)) {
            throw new RuntimeException("Categoría no encontrada");
        }
        categoriaRepository.deleteById(id);
    }
}
