package com.nubix.market.module.product.dto;

/**
 * DTO de respuesta pública del catálogo web.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
public class ProductoPublicResponse {
    /** Identificador único. */
    private Integer id;
    /** Código interno. */
    private String codigo;
    /** Nombre descriptivo. */
    private String nombre;
    /** Descripción detallada. */
    private String descripcion;
    /** Precio de venta. */
    private Double precioVenta;
    /** Cantidad en inventario. */
    private Integer stock;
    /** Nombre de la categoría. */
    private String categoriaNombre;
    /** URL de imagen del producto. */
    private String urlImagen;

    public ProductoPublicResponse(Integer id, String codigo, String nombre, String descripcion,
            Double precioVenta, Integer stock, String categoriaNombre) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioVenta = precioVenta;
        this.stock = stock;
        this.categoriaNombre = categoriaNombre;
    }

    /**
     * Obtiene el identificador.
     * @return resultado de la operación
     */
    public Integer getId() {
        return id;
    }

    /**
     * Establece el identificador.
     * @param id Identificador único.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * GetCodigo.
     * @return resultado de la operación
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * SetCodigo.
     * @param codigo Código interno.
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * GetNombre.
     * @return resultado de la operación
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * SetNombre.
     * @param nombre Nombre descriptivo.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * GetDescripcion.
     * @return resultado de la operación
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * SetDescripcion.
     * @param descripcion Descripción detallada.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * GetPrecioVenta.
     * @return resultado de la operación
     */
    public Double getPrecioVenta() {
        return precioVenta;
    }

    /**
     * SetPrecioVenta.
     * @param precioVenta Precio de venta.
     */
    public void setPrecioVenta(Double precioVenta) {
        this.precioVenta = precioVenta;
    }

    /**
     * GetStock.
     * @return resultado de la operación
     */
    public Integer getStock() {
        return stock;
    }

    /**
     * SetStock.
     * @param stock Cantidad en inventario.
     */
    public void setStock(Integer stock) {
        this.stock = stock;
    }

    /**
     * GetCategoriaNombre.
     * @return resultado de la operación
     */
    public String getCategoriaNombre() {
        return categoriaNombre;
    }

    /**
     * SetCategoriaNombre.
     * @param categoriaNombre Nombre de la categoría.
     */
    public void setCategoriaNombre(String categoriaNombre) {
        this.categoriaNombre = categoriaNombre;
    }

    /**
     * GetUrlImagen.
     * @return resultado de la operación
     */
    public String getUrlImagen() {
        return urlImagen;
    }

    /**
     * SetUrlImagen.
     * @param urlImagen URL de imagen del producto.
     */
    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }
}
