package com.nubix.market.module.supplier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.nubix.market.module.supplier.model.Proveedor;

/**
 * Repositorio de Spring Data JPA para la entidad Proveedor.
 * Incorpora métodos de validación fiscal derivados (Query Methods).
 */
@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Integer> {
    /**
     * Verifica si un RUC específico ya se encuentra registrado en el sistema.
     * Útil al crear un nuevo proveedor para evitar empresas duplicadas.
     *
     * @param ruc El RUC de 11 dígitos.
     * @return true si ya existe.
     */
    boolean existsByRuc(String ruc);

    /**
     * Verifica si un RUC está en uso por OTRO proveedor distinto al especificado.
     * Fundamental durante el proceso de actualización (Update) para evitar que 
     * al editar un proveedor "A", se le asigne por error el RUC del proveedor "B".
     *
     * @param ruc El nuevo RUC propuesto.
     * @param id  El ID del proveedor que se está editando actualmente.
     * @return true si el RUC colisiona con otra entidad diferente.
     */
    boolean existsByRucAndIdNot(String ruc, Integer id);
}
