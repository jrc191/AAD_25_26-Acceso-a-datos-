package com.jramcon398.jrc;

import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
@Slf4j
public class JrcApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(JrcApplication.class, args);
	}

    @Override
    public void run(String... args) throws Exception {
        Path ruta = Paths.get("ejemploNIO.txt");
        if (!Files.exists(ruta)) {
            Files.createFile(ruta);
            log.info("Fichero creado con NIO.2");
        }
        else{
            log.warn("Fichero ya creado con NIO.2 en {}",ruta.getFileName());
        }
        // Escribir texto en el fichero
        Files.write(ruta, "Hola mundo desde NIO.2".getBytes());

        // Leer el contenido
        String contenido = Files.readString(ruta);
        log.info("Contenido: {}", contenido);

    }
}
