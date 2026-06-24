package com.nubix.market.common.jackson;

/**
 * Vistas Jackson para controlar la serialización JSON según el contexto de la respuesta.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
public final class JsonViews {

    private JsonViews() {
    }

    /**
     * Vista para listados con campos resumidos.
     */
    public interface List {
    }

    /**
     * Vista para detalle; incluye todos los campos de {@link List}.
     */
    public interface Detail extends List {
    }
}
