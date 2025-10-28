package com.jramcon398.jrc;

import com.jramcon398.jrc.application.LogService;
import com.jramcon398.jrc.util.InputValidation;
import com.jramcon398.jrc.util.Menu;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the JRC logging application.
 * Implements CommandLineRunner to provide a console-based menu for user interaction.
 * Handles adding log events, filtering by date, changing encoding, and displaying all logs.
 * Uses LogService for business logic and LogRepository for data persistence.
 *
 * @see LogService
 * @see InputValidation
 *
 */

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class JrcApplication implements CommandLineRunner {

    private final Menu menu;

    public static void main(String[] args) {
        SpringApplication.run(JrcApplication.class, args);
    }


    /**
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        menu.execute();
    }

}
