package com.nubix.market;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de ejecución (Entry Point) de la aplicación Spring Boot.
 * Levanta el contenedor de inversión de control (IoC), configura los beans 
 * e inicia el servidor web embebido (Tomcat) para el backend de Nubix Market.
 */
@SpringBootApplication
public class MarketBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarketBackendApplication.class, args);
	}

}
