package com.jramcon398.jrc;

import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
@Slf4j
public class JrcApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(JrcApplication.class, args);
	}

    @Override
    public void run(String... args) throws Exception {
        File file = new File("ejemplo.txt");
        if (file.createNewFile()) {
            log.info("Fichero creado: {}", file.getName());
        } else {
            log.warn("El fichero ya existe.");
            log.info("El fichero existe en: {}", file.getAbsolutePath()); //Ruta del fichero en el disco.
        }
    }
}
