package com.nubix.market.module.notification.service;

import org.springframework.stereotype.Component;

@Component
public class EmailTemplateService {

    private static final String BRAND = "#006634";
    private static final String BRAND_LIGHT = "#e8f5ee";
    private static final String TEXT = "#1a1a1a";
    private static final String MUTED = "#6b7280";

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

    public String confirmacionCompra(String numero, String tipo, String codigoRecojo, Double total) {
        String safeNumero = escape(numero != null ? numero : "-");
        String safeTipo = escape(tipo != null ? tipo : "Comprobante");
        String totalStr = total != null ? String.format("S/ %.2f", total) : "-";

        StringBuilder extra = new StringBuilder();
        if (codigoRecojo != null && !codigoRecojo.isBlank()) {
            extra.append("""
                    <div style="background:%s;border-radius:10px;padding:14px 16px;margin:20px 0;text-align:center;">
                      <p style="margin:0 0 6px;color:%s;font-size:13px;font-weight:600;">Código de recojo</p>
                      <p style="margin:0;color:%s;font-size:22px;font-weight:700;letter-spacing:2px;">%s</p>
                    </div>
                    """.formatted(BRAND_LIGHT, MUTED, BRAND, escape(codigoRecojo)));
        }

        return baseLayout(
                "¡Gracias por tu compra!",
                """
                        <p style="margin:0 0 16px;color:%s;font-size:15px;line-height:1.6;">
                          Tu pedido en <strong>Nubix Market</strong> fue registrado correctamente.
                        </p>
                        <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" style="margin:0 0 20px;background:#fafafa;border-radius:10px;border:1px solid #e5e7eb;">
                          <tr><td style="padding:12px 16px;color:%s;font-size:13px;">Pedido</td><td style="padding:12px 16px;text-align:right;color:%s;font-weight:700;">%s</td></tr>
                          <tr><td style="padding:12px 16px;color:%s;font-size:13px;border-top:1px solid #e5e7eb;">Comprobante</td><td style="padding:12px 16px;text-align:right;color:%s;font-weight:600;border-top:1px solid #e5e7eb;">%s</td></tr>
                          <tr><td style="padding:12px 16px;color:%s;font-size:13px;border-top:1px solid #e5e7eb;">Total pagado</td><td style="padding:12px 16px;text-align:right;color:%s;font-weight:700;font-size:16px;border-top:1px solid #e5e7eb;">%s</td></tr>
                        </table>
                        %s
                        <p style="margin:0;color:%s;font-size:13px;line-height:1.5;">
                          Conserva este correo como referencia de tu compra.
                        </p>
                        """.formatted(
                        TEXT,
                        MUTED, TEXT, safeNumero,
                        MUTED, TEXT, safeTipo,
                        MUTED, BRAND, totalStr,
                        extra.toString(),
                        MUTED));
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
                        <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" style="max-width:520px;background:#ffffff;border-radius:16px;overflow:hidden;box-shadow:0 8px 24px rgba(0,0,0,0.08);">
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
                            <td style="background:#fafafa;padding:16px 28px;text-align:center;border-top:1px solid #e5e7eb;">
                              <p style="margin:0;color:%s;font-size:12px;">© Nubix Market — Calidad y frescura garantizada</p>
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                  </table>
                </body>
                </html>
                """.formatted(BRAND, escape(title), bodyHtml, MUTED);
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
