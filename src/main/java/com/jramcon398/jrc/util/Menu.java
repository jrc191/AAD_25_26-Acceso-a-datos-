package com.jramcon398.jrc.util;

import com.jramcon398.jrc.application.LogService;
import com.jramcon398.jrc.model.LogEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

/**
 * Menu class used in Main app.
 * Provides a console-based menu for user interaction.
 * Handles adding log events, filtering by date, changing encoding, and displaying all logs.
 *
 * @see LogService
 * @see InputValidation
 */

@Slf4j
@Component
@RequiredArgsConstructor
public final class Menu {

    private final Scanner scanner = new Scanner(System.in);
    private final LogService logService;

    public void execute() {

        boolean exit = false;

        while (!exit) {
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

    private void addEvent() {
        log.info("Option 1. Add Event selected.");
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
        log.info("Option 2. Filter by date selected.");
        String dateInput = InputValidation.validateDateInput(scanner, "Enter date (YYYY-MM-DD): ");

        log.info("Filtering logs for date: {}", dateInput);
        List<LogEvent> logs = logService.getEventsByDate(dateInput);

        if (logs.isEmpty()) {
            log.warn("No logs found for the specified date.");
        } else {
            for (LogEvent logEvent : logs) {
                log.info("Log: {}", logEvent);
            }
        }
    }

    private void changeEncoding() {
        log.info("Option 3. Change encoding selected.");
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
                log.warn("Encoding change cancelled.");
                validInput = true;
            } else {
                log.warn("Invalid input. Please enter 'y' for yes or 'n' for no: ");
            }
        } while (!validInput);
    }

    private void showAllLogs() {
        log.info("Option 4. Show all logs selected.");
        List<LogEvent> allLogs = logService.getAllEvents();

        if (allLogs.isEmpty()) {
            log.warn("No logs found.");
        } else {
            log.info("Found {} log events:", allLogs.size());
            for (LogEvent logEvent : allLogs) {
                log.info("Log: {}", logEvent);
            }
        }
    }
}
