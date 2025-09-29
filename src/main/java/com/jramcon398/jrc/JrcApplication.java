package com.jramcon398.jrc;

import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.nio.charset.StandardCharsets;
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
        Path ruta = Paths.get("alumnos.txt");
        // Escribir en el fichero
        Files.writeString(ruta, "ID,Nombre\n1,Ana\n2,Juan", StandardCharsets.UTF_8);
        // Leer del fichero
        String contenido = Files.readString(ruta, StandardCharsets.UTF_8);
        log.info("Contenido del fichero:\n{}", contenido);


    }
}
