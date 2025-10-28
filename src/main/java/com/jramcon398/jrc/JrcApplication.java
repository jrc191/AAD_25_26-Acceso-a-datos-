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

import java.util.List;
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
            int option = InputValidation.validateMenuOption(scanner, 1, 5, "Please select an option (1-5): ");

            switch (option) {
                case 1 -> addEvent();
                case 2 -> filterByDate();
                case 3 -> changeEncoding();
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
        String dateInput = InputValidation.validateDateInput(scanner, "Enter date (YYYY-MM-DD): ");

        log.info("Filtering logs for date: {}", dateInput);

        List<LogEvent> logs = logService.getEventsByDate(dateInput);

        if (logs.isEmpty()) {
            log.info("No logs found for the specified date.");
        } else {
            for (LogEvent logEvent : logs) {
                log.info("Log: {}", logEvent);
            }

        }
    }

    private void changeEncoding() {
        log.info("Current encoding: {}", logService.getCurrentEncoding());

        String encoding = InputValidation.validateEncodingInput(scanner);

        log.warn("WARNING: Changing encoding will affect how new logs are written and existing logs are read.");
        log.info("This may cause character corruption if the current encoding was different.");
        log.info("Continue? (yes (y)/ no (n)): ");

        String confirmation;
        boolean validInput = false;

        do {
            confirmation = scanner.nextLine().trim().toLowerCase();

            if (confirmation.equals("y") || confirmation.equals("yes")) {
                boolean success = logService.changeEncoding(encoding);
                if (success) {
                    log.info("Encoding successfully changed to: {}", encoding);
                } else {
                    log.error("Failed to change encoding");
                }
                validInput = true;
            } else if (confirmation.equals("n") || confirmation.equals("no")) {
                log.info("Encoding change cancelled.");
                validInput = true;
            } else {
                log.warn("Invalid input. Please enter 'y' for yes or 'n' for no: ");
            }
        } while (!validInput);
    }


    private void showAllLogs() {
        List<LogEvent> allLogs = logService.getAllEvents();

        if (allLogs.isEmpty()) {
            log.info("No logs found.");
        } else {
            log.info("Found {} log events:", allLogs.size());
            for (LogEvent logEvent : allLogs) {
                log.info("Log: {}", logEvent);
            }

        }
    }
}
