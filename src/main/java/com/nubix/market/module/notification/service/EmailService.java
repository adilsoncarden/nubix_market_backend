package com.nubix.market.module.notification.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailTemplateService emailTemplateService;

    @Value("${spring.mail.properties.mail.from:noreply@nubixmarket.com}")
    private String fromAddress;

    public void enviarCodigoRecuperacion(String email, String codigo) {
        String html = emailTemplateService.recuperacionContrasena(codigo);
        enviarHtml(email, "Recuperación de contraseña — Nubix Market", html);
    }

    public void enviarConfirmacionCompra(String email, String numero, String tipo, String codigoRecojo, Double total) {
        String html = emailTemplateService.confirmacionCompra(numero, tipo, codigoRecojo, total);
        enviarHtml(email, "Confirmación de compra — " + (numero != null ? numero : "Nubix Market"), html);
    }

    private void enviarHtml(String to, String subject, String html) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromAddress);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("Error enviando email a {}: {}", to, e.getMessage());
            throw new RuntimeException("No se pudo enviar el correo electrónico");
        }
    }
}
