package com.nubix.market.module.sale.dto;

import java.util.List;

/**
 * DTO de solicitud para registrar una compra a proveedor.
 * Incluye el número de factura del proveedor y las líneas de producto adquiridos.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
public class CompraRequest {

    /** Número de factura emitido por el proveedor. */
    private String numeroFactura;

    /** Identificador del proveedor. */
    private Integer proveedorId;

    /** Líneas de producto incluidas en la compra. */
    private List<DetalleCompraRequest> detalles;

    /**
     * Obtiene el número de factura del proveedor.
     *
     * @return número de factura
     */
    public String getNumeroFactura() {
        return numeroFactura;
    }

    /**
     * Establece el número de factura del proveedor.
     *
     * @param numeroFactura número de factura
     */
    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    /**
     * Obtiene el identificador del proveedor.
     *
     * @return id del proveedor
     */
    public Integer getProveedorId() {
        return proveedorId;
    }

    /**
     * Establece el identificador del proveedor.
     *
     * @param proveedorId id del proveedor
     */
    public void setProveedorId(Integer proveedorId) {
        this.proveedorId = proveedorId;
    }

    /**
     * Obtiene las líneas de la compra.
     *
     * @return lista de detalles de compra
     */
    public List<DetalleCompraRequest> getDetalles() {
        return detalles;
    }

    /**
     * Establece las líneas de la compra.
     *
     * @param detalles lista de detalles de compra
     */
    public void setDetalles(List<DetalleCompraRequest> detalles) {
        this.detalles = detalles;
    }

    /**
     * Línea de producto incluida en una solicitud de compra a proveedor.
     *
     * @author Grupo de Desarrollo Nubix Market
     * @version 1.0.0 (2026)
     */
    public static class DetalleCompraRequest {

        /** Código del producto en inventario. */
        private String codigoProducto;

        /** Nombre del producto a registrar o actualizar. */
        private String nombreProducto;

        /** Descripción del producto. */
        private String descripcion;

        /** Identificador de la categoría del producto. */
        private Integer categoriaId;

        /** Cantidad adquirida al proveedor. */
        private Integer cantidad;

        /** Precio unitario de compra. */
        private Double precioCompra;

        /**
         * Obtiene el código del producto.
         *
         * @return código del producto
         */
        public String getCodigoProducto() {
            return codigoProducto;
        }

        /**
         * Establece el código del producto.
         *
         * @param codigoProducto código del producto
         */
        public void setCodigoProducto(String codigoProducto) {
            this.codigoProducto = codigoProducto;
        }

        /**
         * Obtiene el nombre del producto.
         *
         * @return nombre del producto
         */
        public String getNombreProducto() {
            return nombreProducto;
        }

        /**
         * Establece el nombre del producto.
         *
         * @param nombreProducto nombre del producto
         */
        public void setNombreProducto(String nombreProducto) {
            this.nombreProducto = nombreProducto;
        }

        /**
         * Obtiene la descripción del producto.
         *
         * @return descripción
         */
        public String getDescripcion() {
            return descripcion;
        }

        /**
         * Establece la descripción del producto.
         *
         * @param descripcion descripción
         */
        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

        /**
         * Obtiene el identificador de la categoría.
         *
         * @return id de la categoría
         */
        public Integer getCategoriaId() {
            return categoriaId;
        }

        /**
         * Establece el identificador de la categoría.
         *
         * @param categoriaId id de la categoría
         */
        public void setCategoriaId(Integer categoriaId) {
            this.categoriaId = categoriaId;
        }

        /**
         * Obtiene la cantidad adquirida.
         *
         * @return cantidad de unidades
         */
        public Integer getCantidad() {
            return cantidad;
        }

        /**
         * Establece la cantidad adquirida.
         *
         * @param cantidad cantidad de unidades
         */
        public void setCantidad(Integer cantidad) {
            this.cantidad = cantidad;
        }

        /**
         * Obtiene el precio unitario de compra.
         *
         * @return precio de compra por unidad
         */
        public Double getPrecioCompra() {
            return precioCompra;
        }

        /**
         * Establece el precio unitario de compra.
         *
         * @param precioCompra precio de compra por unidad
         */
        public void setPrecioCompra(Double precioCompra) {
            this.precioCompra = precioCompra;
        }
    }
}
