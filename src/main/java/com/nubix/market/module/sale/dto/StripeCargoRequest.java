package com.nubix.market.module.sale.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * DTO de solicitud para procesar un cargo con tarjeta vía Stripe y registrar el pedido web.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
public class StripeCargoRequest {

    /** PaymentMethod ID generado por Stripe.js (pm_...). */
    @NotBlank(message = "El método de pago es obligatorio")
    private String paymentMethodId;

    /** Correo del tarjetahabiente enviado a Stripe. */
    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "Formato de correo inválido")
    private String email;

    /** Monto total de la venta en soles (PEN), con decimales. */
    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser mayor a cero")
    private Double monto;

    /** Datos del checkout para crear el pedido tras un cargo exitoso. */
    @Valid
    @NotNull(message = "Los datos del checkout son obligatorios")
    private CheckoutRequest checkout;

    public String getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(String paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public CheckoutRequest getCheckout() {
        return checkout;
    }

    public void setCheckout(CheckoutRequest checkout) {
        this.checkout = checkout;
    }
}
