package com.jramcon398.jrc;

import com.jramcon398.jrc.manager.FileManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/// CLASE QUE EJECUTA LA APLICACIÓN. USA LA CLASE  FILEMANAGER PARA ESO

@SpringBootApplication
@Slf4j
public class JrcApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(JrcApplication.class, args);
	}


    @Override
    public void run(String... args) throws Exception {

        FileManager fileManager = new FileManager();

        fileManager.menu();

    }


}
