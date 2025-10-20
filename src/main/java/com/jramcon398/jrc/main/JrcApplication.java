package com.jramcon398.jrc.main;

import com.jramcon398.jrc.utils.Menu;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * Main class for the Converter application.
 */

@SpringBootApplication
@Slf4j
public class JrcApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(JrcApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Application started.");
        Menu menu = new Menu();
        menu.executeMenu();

        log.info("Application finished.");
    }
}
