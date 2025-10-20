package com.jramcon398.jrc.main;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JrcApplication implements CommandLineRunner {

	public static void main(String[] args) {
        SpringApplication.run(JrcApplication.class, args);
	}

    @Override
    public void run(String... args) throws Exception {

    }
}
