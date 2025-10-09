package com.jramcon398.jrc.main;

import com.jramcon398.jrc.registryManager.RegistryManager;
import com.jramcon398.jrc.utils.FileUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class JrcApplication implements CommandLineRunner {

	public static void main(String[] args) {
        SpringApplication.run(JrcApplication.class, args);

	}

    @Override
    public void run(String... args) throws Exception {
        File file = new File("students.dat");
        FileUtils.ensureFileExists(file);
        RegistryManager manager = new RegistryManager();
        manager.menu(file);
    }
}
