package com.jramcon398;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for JRC Application.
 */

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class JrcApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(JrcApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Application started
    }
}
