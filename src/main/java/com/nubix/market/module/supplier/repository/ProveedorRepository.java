package com.nubix.market.module.supplier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.nubix.market.module.supplier.model.Proveedor;

/**
 * Repositorio JPA para proveedores.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Integer> {
    /**
     * Verifica existencia por RUC.
     * @param ruc Número de RUC.
     * @return resultado de la operación
     */
    boolean existsByRuc(String ruc);

    /**
     * Verifica RUC duplicado excluyendo id.
     * @param ruc Número de RUC.
     * @param id Identificador único.
     * @return resultado de la operación
     */
    boolean existsByRucAndIdNot(String ruc, Integer id);
}
