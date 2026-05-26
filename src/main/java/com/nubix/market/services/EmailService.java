package com.nubix.market.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void enviarCodigoRecuperacion(String email, String codigo) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Código de recuperación de contraseña");
        message.setText("Tu código de recuperación de contraseña es: " + codigo);
        mailSender.send(message);
    }

    public void enviarConfirmacionCompra(String email, String numero, String tipo, String codigoRecojo, Double total) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Confirmación de compra - " + numero);

        StringBuilder body = new StringBuilder();
        body.append("Gracias por tu compra.\n\n");
        body.append("Comprobante: ").append(tipo != null ? tipo : "-").append("\n");
        body.append("Pedido: ").append(numero != null ? numero : "-").append("\n");
        if (codigoRecojo != null && !codigoRecojo.isBlank()) {
            body.append("Código de recojo: ").append(codigoRecojo).append("\n");
        }
        if (total != null) {
            body.append("Total: S/ ").append(String.format("%.2f", total)).append("\n");
        }
        body.append("\nNubix Market");

        message.setText(body.toString());
        mailSender.send(message);
    }
}
