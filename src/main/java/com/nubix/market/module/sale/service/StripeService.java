package com.nubix.market.module.sale.service;

import com.nubix.market.module.sale.exception.StripePaymentException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Cliente del SDK oficial de Stripe para crear y confirmar PaymentIntents en modo Sandbox.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@Service
public class StripeService {

    private static final Logger log = LoggerFactory.getLogger(StripeService.class);

    @Value("${stripe.api.secret.key:}")
    private String secretKey;

    @PostConstruct
    public void initStripe() {
        if (secretKey != null && !secretKey.isBlank()) {
            Stripe.apiKey = secretKey.trim();
        }
    }

    /**
     * Crea un PaymentIntent en PEN y lo confirma de inmediato con el PaymentMethod del cliente.
     *
     * @param paymentMethodId identificador pm_... generado por Stripe.js
     * @param email           correo para recibo Stripe
     * @param amountCentavos  monto en céntimos (PEN)
     * @return identificador del PaymentIntent (pi_...)
     * @throws StripePaymentException si la clave no está configurada o Stripe rechaza el pago
     */
    public String createAndConfirmPayment(String paymentMethodId, String email, long amountCentavos) {
        if (secretKey == null || secretKey.isBlank()) {
            throw new StripePaymentException(
                    "El procesador de pagos no está configurado. Contacte al administrador.");
        }

        if (amountCentavos <= 0) {
            throw new StripePaymentException("El monto del pago debe ser mayor a cero.");
        }

        try {
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amountCentavos)
                    .setCurrency("pen")
                    .setPaymentMethod(paymentMethodId.trim())
                    .setConfirm(true)
                    .setReceiptEmail(email.trim())
                    .setAutomaticPaymentMethods(
                            PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                    .setEnabled(true)
                                    .setAllowRedirects(
                                            PaymentIntentCreateParams.AutomaticPaymentMethods.AllowRedirects.NEVER)
                                    .build())
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);
            String status = paymentIntent.getStatus();

            if (!"succeeded".equals(status)) {
                log.warn("PaymentIntent {} no completado: estado {}", paymentIntent.getId(), status);
                throw new StripePaymentException(
                        "El pago requiere una acción adicional o no pudo completarse. "
                                + "Verifica los datos de tu tarjeta e intenta de nuevo.",
                        status);
            }

            log.info("Cargo Stripe exitoso: {}", paymentIntent.getId());
            return paymentIntent.getId();
        } catch (StripePaymentException ex) {
            throw ex;
        } catch (StripeException ex) {
            log.warn("Stripe rechazó el pago: {} ({})", ex.getMessage(), ex.getCode());
            throw new StripePaymentException(resolveStripeErrorMessage(ex), ex.getCode());
        } catch (Exception ex) {
            log.error("Error inesperado al contactar Stripe", ex);
            throw new StripePaymentException(
                    "No se pudo procesar el pago en este momento. Intenta nuevamente en unos segundos.");
        }
    }

    private String resolveStripeErrorMessage(StripeException ex) {
        String message = ex.getUserMessage();
        if (message != null && !message.isBlank()) {
            return message;
        }
        if (ex.getMessage() != null && !ex.getMessage().isBlank()) {
            return ex.getMessage();
        }
        return "Tu tarjeta fue rechazada o el pago no pudo completarse. Revisa los datos e intenta de nuevo.";
    }
}
