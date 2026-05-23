package com.nubix.market.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/media")
@CrossOrigin(origins = "*")
public class MediaController {
    @Value("${media.upload-dir}")
    private String uploadDir;
    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "El archivo está vacío"));
        }
        try {
            // 1. Crear la carpeta si no existe
            Path directorioDestino = Paths.get(uploadDir);
            if (!Files.exists(directorioDestino)) {
                Files.createDirectories(directorioDestino);
            }
            // 2. generar un nombre único para la foto (ara que no se sobreescriban)
            String nombreArchivo = UUID.randomUUID().toString() + "_" + file.getOriginalFilename().replaceAll(" ", "_");
            Path rutaFinal = directorioDestino.resolve(nombreArchivo);
            // 3. guardar el archivo en el disco duro
            Files.copy(file.getInputStream(), rutaFinal);
            // 4. devolverle a React la URL de la imagen guardada
            String urlImagen = "/uploads/productos/" + nombreArchivo;
            
            return ResponseEntity.ok(Map.of("url", urlImagen));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", "Error al guardar la imagen"));
        }
    }
}
