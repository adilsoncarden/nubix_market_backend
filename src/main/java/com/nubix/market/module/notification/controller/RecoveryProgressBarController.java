package com.nubix.market.module.notification.controller;

import com.nubix.market.module.notification.service.RecoveryProgressBarService;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/email")
public class RecoveryProgressBarController {

    private final RecoveryProgressBarService recoveryProgressBarService;

    public RecoveryProgressBarController(RecoveryProgressBarService recoveryProgressBarService) {
        this.recoveryProgressBarService = recoveryProgressBarService;
    }

    /**
     * Imagen dinámica de la barra: Gmail y otros clientes la solicitan al abrir el correo
     * y el servidor calcula el ancho según el tiempo restante real.
     */
    @GetMapping(value = "/recovery-bar", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> recoveryBarImage(
            @RequestParam("id") long tokenId,
            @RequestParam("sig") String signature) {
        byte[] png = recoveryProgressBarService.renderBarImage(tokenId, signature);
        return ResponseEntity.ok()
                .cacheControl(CacheControl.noStore())
                .header(HttpHeaders.PRAGMA, "no-cache")
                .contentType(MediaType.IMAGE_PNG)
                .body(png);
    }

    /**
     * Vista HTML con tabla clásica (útil para depuración o clientes que abran el enlace).
     */
    @GetMapping(value = "/recovery-bar", params = "fmt=html", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> recoveryBarHtml(
            @RequestParam("id") long tokenId,
            @RequestParam("sig") String signature) {
        String table = recoveryProgressBarService.renderBarTableHtml(tokenId, signature);
        String html = """
                <!DOCTYPE html>
                <html lang="es"><body style="margin:16px;font-family:Arial,sans-serif;">
                %s
                </body></html>
                """.formatted(table);
        return ResponseEntity.ok()
                .cacheControl(CacheControl.noStore())
                .contentType(MediaType.TEXT_HTML)
                .body(html);
    }
}
