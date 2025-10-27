package com.jramcon398.jrc;

import com.jramcon398.jrc.application.LogService;
import com.jramcon398.jrc.model.LogEvent;
import com.jramcon398.jrc.repository.LogRepository;
import com.jramcon398.jrc.util.InputValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class JrcApplication implements CommandLineRunner {

    private final LogService logService;
    private final LogRepository logRepository;
    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        SpringApplication.run(JrcApplication.class, args);
    }


    /**
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {

        boolean exit = false;

        while (!exit) {
            showMenu();
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> addEvent();
                case 2 -> filterByDate();
                case 3 -> log.info("TODO");//changeEncoding();
                case 4 -> showAllLogs();
                case 5 -> exit = true;
                default -> log.warn("Invalid option. Please try again.");
            }
        }
    }


    private void showMenu() {
        // Mostrar opciones
        log.info("1. Add Event");
        log.info("2. Filter by Date");
        log.info("3. Change Encoding");
        log.info("4. Show All Logs");
        log.info("5. Exit");
        log.info("Select an option: ");

    }

    private void addEvent() {

        String message = InputValidation.validateNonEmptyInput(scanner, "Log Message", "Enter log message: ");

        if (!InputValidation.isValidLogMessage(message)) {
            log.warn("Invalid log message format.");
            return;
        }

        LogEvent created = logService.addLogEvent(message.trim());

        if (created != null) {
            log.info("Log event added successfully: {}", created);
        } else {
            log.error("Failed to add log event");
        }
    }

    private void filterByDate() {
        //TODO
    }

    private void changeEncoding() {
        log.info("Current encoding: {}", logService.getCurrentEncoding());

        String encoding = InputValidation.validateEncodingInput(scanner);

        log.warn("WARNING: Changing encoding will convert ALL existing logs.");
        log.info("This may cause character corruption if the current encoding was different.");
        log.info("Continue? (yes/no): ");

        String confirmation = scanner.nextLine().trim().toLowerCase();
        if (confirmation.equals("yes") || confirmation.equals("y")) {
            boolean success = logService.changeEncoding(encoding);
            if (success) {
                log.info("Encoding successfully changed to: {}", encoding);
            } else {
                log.error("Failed to change encoding");
            }
        } else {
            log.info("Encoding change cancelled.");
        }
    }


    private void showAllLogs() {
        //TODO
    }
}
