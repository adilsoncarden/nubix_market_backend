package com.nubix.market.common.jackson;

/**
 * Clase de utilidad que define las interfaces (vistas) para la serialización
 * y deserialización condicional de objetos JSON utilizando Jackson (@JsonView).
 * Permite controlar qué campos de una entidad se exponen en las respuestas de la API.
 */
public final class JsonViews {

    /**
     * Constructor privado para evitar la instanciación de esta clase de utilidad.
     */
    private JsonViews() {
    }

    /**
     * Vista básica utilizada para respuestas que devuelven listas o colecciones.
     * Expone únicamente los campos esenciales y de menor peso (ej. id, nombre).
     */
    public interface List {
    }

    /**
     * Vista detallada utilizada para respuestas de un único objeto completo.
     * Hereda de {@link List}, por lo que expone los campos básicos junto con 
     * todos los detalles adicionales de la entidad.
     */
    public interface Detail extends List {
    }
}
