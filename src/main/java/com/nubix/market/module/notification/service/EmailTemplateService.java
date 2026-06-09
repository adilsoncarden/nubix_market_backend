package com.nubix.market.module.notification.service;

import com.nubix.market.module.notification.dto.EmailConfirmacionContext;
import com.nubix.market.module.notification.dto.EmailProductoLinea;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmailTemplateService {

    private static final String BRAND = "#006634";
    private static final String BRAND_LIGHT = "#e8f5ee";
    private static final String TEXT = "#1a1a1a";
    private static final String MUTED = "#6b7280";
    private static final String BORDER = "#e5e7eb";

    public String recuperacionContrasena(String codigo) {
        String safeCodigo = escape(codigo);
        return baseLayout(
                "Recuperación de contraseña",
                """
                        <p style="margin:0 0 16px;color:%s;font-size:15px;line-height:1.6;">
                          Recibimos una solicitud para restablecer tu contraseña en <strong>Nubix Market</strong>.
                        </p>
                        <p style="margin:0 0 20px;color:%s;font-size:14px;line-height:1.6;">
                          Usa el siguiente código de verificación. Expira en pocos minutos.
                        </p>
                        <div style="text-align:center;margin:28px 0;">
                          <span style="display:inline-block;background:%s;color:%s;font-size:28px;font-weight:700;letter-spacing:6px;padding:16px 28px;border-radius:12px;border:2px dashed %s;">
                            %s
                          </span>
                        </div>
                        <p style="margin:0;color:%s;font-size:13px;line-height:1.5;">
                          Si no solicitaste este cambio, ignora este correo. Tu cuenta permanecerá segura.
                        </p>
                        """.formatted(TEXT, MUTED, BRAND_LIGHT, BRAND, BRAND, safeCodigo, MUTED));
    }

    public String confirmacionCompra(EmailConfirmacionContext ctx) {
        String safeNumero = escape(ctx.getNumero() != null ? ctx.getNumero() : "-");
        String safeTipo = escape(ctx.getTipoComprobante() != null ? ctx.getTipoComprobante() : "Comprobante");
        String productosTable = buildProductosTable(ctx.getProductos());
        String totalesTable = buildTotalesTable(ctx);
        String codigoRecojoBlock = buildCodigoRecojoBlock(ctx.getCodigoRecojo());

        return baseLayout(
                "¡Gracias por tu compra!",
                """
                        <p style="margin:0 0 16px;color:%s;font-size:15px;line-height:1.6;">
                          Tu pedido en <strong>Nubix Market</strong> fue registrado correctamente.
                          A continuación encontrarás el detalle de tu compra.
                        </p>
                        <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" style="margin:0 0 20px;background:#fafafa;border-radius:10px;border:1px solid %s;">
                          <tr>
                            <td style="padding:12px 16px;color:%s;font-size:13px;">Pedido</td>
                            <td style="padding:12px 16px;text-align:right;color:%s;font-weight:700;">%s</td>
                          </tr>
                          <tr>
                            <td style="padding:12px 16px;color:%s;font-size:13px;border-top:1px solid %s;">Comprobante</td>
                            <td style="padding:12px 16px;text-align:right;color:%s;font-weight:600;border-top:1px solid %s;">%s</td>
                          </tr>
                        </table>
                        <p style="margin:0 0 10px;color:%s;font-size:14px;font-weight:700;">Detalle de productos</p>
                        %s
                        %s
                        %s
                        <p style="margin:16px 0 0;color:%s;font-size:13px;line-height:1.5;">
                          Conserva este correo como referencia de tu compra. Si tienes dudas, contáctanos.
                        </p>
                        """.formatted(
                        TEXT,
                        BORDER,
                        MUTED, TEXT, safeNumero,
                        MUTED, BORDER, TEXT, BORDER, safeTipo,
                        TEXT,
                        productosTable,
                        totalesTable,
                        codigoRecojoBlock,
                        MUTED));
    }

    private String buildProductosTable(List<EmailProductoLinea> productos) {
        if (productos == null || productos.isEmpty()) {
            return """
                    <p style="margin:0 0 16px;color:%s;font-size:13px;font-style:italic;">
                      No hay productos detallados en este pedido.
                    </p>
                    """.formatted(MUTED);
        }

        StringBuilder rows = new StringBuilder();
        for (EmailProductoLinea linea : productos) {
            rows.append("""
                    <tr>
                      <td style="padding:12px 14px;border-bottom:1px solid %s;color:%s;font-size:13px;line-height:1.4;">%s</td>
                      <td style="padding:12px 10px;border-bottom:1px solid %s;text-align:center;color:%s;font-size:13px;width:70px;">%d</td>
                      <td style="padding:12px 14px;border-bottom:1px solid %s;text-align:right;color:%s;font-weight:600;font-size:13px;width:100px;">%s</td>
                    </tr>
                    """.formatted(
                    BORDER, TEXT, escape(linea.getNombre()),
                    BORDER, TEXT, linea.getCantidad(),
                    BORDER, BRAND, formatSoles(linea.getSubtotal())));
        }

        return """
                <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" style="margin:0 0 20px;border:1px solid %s;border-radius:10px;overflow:hidden;border-collapse:collapse;">
                  <thead>
                    <tr style="background:%s;">
                      <th align="left" style="padding:12px 14px;color:%s;font-size:12px;font-weight:700;text-transform:uppercase;letter-spacing:0.4px;">Producto</th>
                      <th align="center" style="padding:12px 10px;color:%s;font-size:12px;font-weight:700;text-transform:uppercase;letter-spacing:0.4px;">Cant.</th>
                      <th align="right" style="padding:12px 14px;color:%s;font-size:12px;font-weight:700;text-transform:uppercase;letter-spacing:0.4px;">Subtotal</th>
                    </tr>
                  </thead>
                  <tbody>
                    %s
                  </tbody>
                </table>
                """.formatted(BORDER, BRAND_LIGHT, BRAND, BRAND, BRAND, rows.toString());
    }

    private String buildTotalesTable(EmailConfirmacionContext ctx) {
        StringBuilder rows = new StringBuilder();

        if (ctx.getSubtotal() != null) {
            rows.append(totalRow("Subtotal (sin IGV)", formatSoles(ctx.getSubtotal()), false));
        }
        if (ctx.getIgv() != null) {
            rows.append(totalRow("IGV (13%)", formatSoles(ctx.getIgv()), false));
        }
        if (ctx.getCostoEnvio() != null && ctx.getCostoEnvio() > 0) {
            rows.append(totalRow("Envío", formatSoles(ctx.getCostoEnvio()), false));
        }
        rows.append(totalRow("Total", formatSoles(ctx.getTotal()), true));

        return """
                <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" style="margin:0 0 20px;background:#fafafa;border-radius:10px;border:1px solid %s;">
                  %s
                </table>
                """.formatted(BORDER, rows.toString());
    }

    private String totalRow(String label, String value, boolean highlight) {
        String valueStyle = highlight
                ? "color:%s;font-weight:700;font-size:16px;".formatted(BRAND)
                : "color:%s;font-weight:600;font-size:13px;".formatted(TEXT);
        String labelWeight = highlight ? "font-weight:700;" : "font-weight:600;";
        return """
                <tr>
                  <td style="padding:12px 16px;color:%s;font-size:13px;%s;border-top:1px solid %s;">%s</td>
                  <td style="padding:12px 16px;text-align:right;%s border-top:1px solid %s;">%s</td>
                </tr>
                """.formatted(MUTED, labelWeight, BORDER, escape(label), valueStyle, BORDER, value);
    }

    private String buildCodigoRecojoBlock(String codigoRecojo) {
        if (codigoRecojo == null || codigoRecojo.isBlank()) {
            return "";
        }
        return """
                <div style="background:%s;border-radius:10px;padding:16px 18px;margin:0 0 20px;text-align:center;border:1px solid %s;">
                  <p style="margin:0 0 8px;color:%s;font-size:13px;font-weight:600;text-transform:uppercase;letter-spacing:0.5px;">Código de recojo</p>
                  <p style="margin:0;color:%s;font-size:26px;font-weight:700;letter-spacing:3px;">%s</p>
                  <p style="margin:8px 0 0;color:%s;font-size:12px;">Presenta este código al retirar tu pedido en tienda.</p>
                </div>
                """.formatted(BRAND_LIGHT, BORDER, MUTED, BRAND, escape(codigoRecojo), MUTED);
    }

    private String formatSoles(Double amount) {
        if (amount == null) {
            return "—";
        }
        return String.format("S/ %.2f", amount);
    }

    private String formatSoles(double amount) {
        return String.format("S/ %.2f", amount);
    }

    private String baseLayout(String title, String bodyHtml) {
        return """
                <!DOCTYPE html>
                <html lang="es">
                <head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1"></head>
                <body style="margin:0;padding:0;background:#f4f6f8;font-family:Arial,Helvetica,sans-serif;">
                  <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" style="background:#f4f6f8;padding:32px 16px;">
                    <tr>
                      <td align="center">
                        <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" style="max-width:560px;background:#ffffff;border-radius:16px;overflow:hidden;box-shadow:0 8px 24px rgba(0,0,0,0.08);">
                          <tr>
                            <td style="background:%s;padding:24px 28px;text-align:center;">
                              <p style="margin:0;color:#ffffff;font-size:22px;font-weight:700;letter-spacing:1px;">NUBIX MARKET</p>
                              <p style="margin:8px 0 0;color:rgba(255,255,255,0.9);font-size:14px;">%s</p>
                            </td>
                          </tr>
                          <tr>
                            <td style="padding:28px;">%s</td>
                          </tr>
                          <tr>
                            <td style="background:#fafafa;padding:16px 28px;text-align:center;border-top:1px solid %s;">
                              <p style="margin:0;color:%s;font-size:12px;">© Nubix Market — Calidad y frescura garantizada</p>
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                  </table>
                </body>
                </html>
                """.formatted(BRAND, escape(title), bodyHtml, BORDER, MUTED);
    }

    private static String escape(String value) {
        if (value == null) {
            return "";
        }
        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}
