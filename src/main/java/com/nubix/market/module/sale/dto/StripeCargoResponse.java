package com.nubix.market.module.sale.dto;

import com.nubix.market.module.sale.model.Venta;

/**
 * Respuesta del cargo Stripe con la venta registrada en Nubix Market.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
public class StripeCargoResponse {

    /** Identificador del PaymentIntent en Stripe (pi_...). */
    private String stripePaymentIntentId;

    /** Venta creada tras el cobro exitoso. */
    private Venta venta;

    public StripeCargoResponse() {
    }

    public StripeCargoResponse(String stripePaymentIntentId, Venta venta) {
        this.stripePaymentIntentId = stripePaymentIntentId;
        this.venta = venta;
    }

    public String getStripePaymentIntentId() {
        return stripePaymentIntentId;
    }

    public void setStripePaymentIntentId(String stripePaymentIntentId) {
        this.stripePaymentIntentId = stripePaymentIntentId;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }
}
