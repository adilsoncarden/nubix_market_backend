package com.nubix.market.module.sale.dto;

import java.util.List;

/**
 * Objeto de Transferencia de Datos (DTO) para registrar compras a proveedores (Abastecimiento).
 * NOTA: Aunque este archivo esté en el módulo de 'sale' (ventas), parece ser utilizado 
 * para el ingreso de mercadería al almacén.
 */
public class CompraRequest {

    /** Número de la factura emitida por el proveedor. */
    private String numeroFactura;

    /** Identificador del proveedor en la base de datos. */
    private Integer proveedorId;

    /** Lista de productos adquiridos para reabastecer el stock. */
    private List<DetalleCompraRequest> detalles;

    // Getters and Setters
    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public Integer getProveedorId() {
        return proveedorId;
    }

    public void setProveedorId(Integer proveedorId) {
        this.proveedorId = proveedorId;
    }

    public List<DetalleCompraRequest> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleCompraRequest> detalles) {
        this.detalles = detalles;
    }

    /**
     * Sub-clase DTO que representa una línea de producto ingresando al inventario.
     */
    public static class DetalleCompraRequest {
        private String codigoProducto;
        private String nombreProducto;
        private String descripcion;
        private Integer categoriaId;
        private Integer cantidad;
        private Double precioCompra;

        // Getters and Setters
        public String getCodigoProducto() {
            return codigoProducto;
        }

        public void setCodigoProducto(String codigoProducto) {
            this.codigoProducto = codigoProducto;
        }

        public String getNombreProducto() {
            return nombreProducto;
        }

        public void setNombreProducto(String nombreProducto) {
            this.nombreProducto = nombreProducto;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

        public Integer getCategoriaId() {
            return categoriaId;
        }

        public void setCategoriaId(Integer categoriaId) {
            this.categoriaId = categoriaId;
        }

        public Integer getCantidad() {
            return cantidad;
        }

        public void setCantidad(Integer cantidad) {
            this.cantidad = cantidad;
        }

        public Double getPrecioCompra() {
            return precioCompra;
        }

        public void setPrecioCompra(Double precioCompra) {
            this.precioCompra = precioCompra;
        }
    }
}
