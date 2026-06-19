package com.nubix.market.module.notification.service;

import com.nubix.market.module.notification.dto.EmailConfirmacionContext;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.io.UnsupportedEncodingException;

/**
 * Servicio encargado de gestionar el envío físico de correos electrónicos a través del protocolo SMTP.
 * Encapsula la complejidad de configurar los mensajes MIME, las cabeceras HTML y los remitentes.
 */
@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private static final String FROM_DISPLAY_NAME = "NUBIX MARKET";

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailTemplateService emailTemplateService;

    @Value("${spring.mail.from:${spring.mail.username:}}")
    private String fromAddress;

    /**
     * Coordina la creación del HTML y el envío del correo que contiene el código 
     * de verificación de 6 dígitos para recuperar una contraseña.
     *
     * @param email Correo electrónico destinatario.
     * @param codigo El código de seguridad temporal.
     */
    public void enviarCodigoRecuperacion(String email, String codigo) {
        String html = emailTemplateService.recuperacionContrasena(codigo);
        enviarHtml(email, "Recuperación de contraseña — Nubix Market", html);
    }

    /**
     * Coordina la creación del HTML y el envío de la boleta/recibo digital de una compra.
     *
     * @param context Objeto con todos los detalles de la venta listos para la plantilla.
     */
    public void enviarConfirmacionCompra(EmailConfirmacionContext context) {
        String html = emailTemplateService.confirmacionCompra(context);
        String numero = context.getNumero() != null ? context.getNumero() : "Nubix Market";
        enviarHtml(context.getEmail(), "Confirmación de compra — " + numero, html);
    }

    /**
     * Método central privado que ejecuta el envío real del correo electrónico HTML utilizando JavaMailSender.
     * Configura el remitente oficial y el Asunto del correo.
     *
     * @param to Correo del destinatario.
     * @param subject Asunto descriptivo del mensaje.
     * @param html Contenido del correo renderizado como un string HTML.
     * @throws RuntimeException Si ocurre algún error en la conexión SMTP o al generar las cabeceras.
     */
    private void enviarHtml(String to, String subject, String html) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
            helper.setFrom(buildFromAddress());
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);
            mailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("Error enviando email a {}: {}", to, e.getMessage());
            throw new RuntimeException("No se pudo enviar el correo electrónico");
        }
    }

    /**
     * Construye la identidad visual del remitente. El usuario verá "NUBIX MARKET" 
     * en su bandeja de entrada en lugar de una simple dirección de correo sin formato.
     */
    private InternetAddress buildFromAddress() throws UnsupportedEncodingException {
        String address = fromAddress != null && !fromAddress.isBlank()
                ? fromAddress.trim()
                : "noreply@nubixmarket.com";
        return new InternetAddress(address, FROM_DISPLAY_NAME, StandardCharsets.UTF_8.name());
    }
}
