package com.nubix.market.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.nio.file.Path;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Obtenemos la ruta absoluta de la carpeta "uploads" en tu PC
        String ruta = Path.of("uploads")
        .toFile()
        .getAbsolutePath();
        // Le decimos a Spring que permita acceder a los archivos de esa carpeta
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:/" + ruta + "/");
    }
}
