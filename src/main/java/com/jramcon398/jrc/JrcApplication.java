package com.jramcon398.jrc;

import com.jramcon398.jrc.repository.PostgresqlDriver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class JrcApplication implements CommandLineRunner {


    private final PostgresqlDriver postgresqlDriver;

    public static void main(String[] args) {
        SpringApplication.run(JrcApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Testing JDBC connection...");
        try (Connection conn = postgresqlDriver.getConnection()) {
            log.info("Connection successful: {}",
                    conn.getMetaData().getURL());
            log.info("Database: {}",
                    conn.getMetaData().getDatabaseProductName());

            postgresqlDriver.init();
            
        } catch (Exception e) {
            log.error("Connection failed: {}", e.getMessage());
        }
    }


}
