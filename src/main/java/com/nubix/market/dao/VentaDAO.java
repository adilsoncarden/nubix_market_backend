package com.nubix.market.dao;

import com.nubix.market.entities.Venta;

import java.time.LocalDate;
import java.util.List;

/**
 * DAO de ejemplo: consultas personalizadas para reportes.
 * Los repositorios Spring Data ({@code JpaRepository}) son la implementación estándar de acceso a datos;
 * este contrato aísla consultas específicas (p. ej. filtros por rango de fechas) sin sustituir al repositorio.
 */
public interface VentaDAO {

    List<Venta> buscarVentasEntreFechas(LocalDate desde, LocalDate hasta);
}
