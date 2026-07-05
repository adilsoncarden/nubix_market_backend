package com.nubix.market.module.sale.exception;

/**
 * Excepción de negocio para errores devueltos por Stripe o validaciones del cargo.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
public class StripePaymentException extends RuntimeException {

    private final String stripeCode;

    public StripePaymentException(String message) {
        super(message);
        this.stripeCode = null;
    }

    public StripePaymentException(String message, String stripeCode) {
        super(message);
        this.stripeCode = stripeCode;
    }

    public String getStripeCode() {
        return stripeCode;
    }
}
