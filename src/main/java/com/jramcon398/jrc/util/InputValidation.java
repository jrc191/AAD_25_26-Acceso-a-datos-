package com.jramcon398.jrc.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/**
 * InputValidation utility class providing methods for validating user inputs.
 * Includes methods for menu option validation, non-empty input validation,
 * date input validation, log message validation, and encoding input validation.
 */

@Slf4j
@UtilityClass
public class InputValidation {

    private void showMenu() {
        // Mostrar opciones
        log.info("1. Add Event");
        log.info("2. Filter by Date");
        log.info("3. Change Encoding");
        log.info("4. Show All Logs");
        log.info("5. Exit");

    }

    //Reusable method to validate menu option input. If needed can be extended to accept different ranges.
    public int validateMenuOption(Scanner scanner, int minOption, int maxOption, String prompt) {
        showMenu();
        int option = -1;
        do {
            log.info(prompt);
            while (!scanner.hasNextInt()) {
                log.warn("Invalid input. Please enter a number between {} and {}.", minOption, maxOption);
                scanner.next(); // clear invalid input
            }
            option = scanner.nextInt();
            scanner.nextLine(); // consume newline
            if (option < minOption || option > maxOption) {
                log.warn("Option out of range. Please enter a number between {} and {}.", minOption, maxOption);
            }
        } while (option < minOption || option > maxOption);

        return option;
    }

    public String validateNonEmptyInput(Scanner scanner, String fieldName, String prompt) {
        String input = "";
        do {
            log.info(prompt);
            input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                log.warn("{} cannot be empty. Please try again.", fieldName);
            }
        } while (input.isEmpty());

        return input;
    }

    public String validateDateInput(Scanner scanner, String prompt) {

        String date = "";
        boolean valid = false;

        do {
            log.info(prompt);
            date = scanner.nextLine().trim();

            try {
                LocalDate parsedDate = LocalDate.parse(date, Constant.DATE_FORMAT);
                valid = true;
                return parsedDate.format(Constant.DATE_FORMAT); // return normalized string
            } catch (DateTimeParseException e) {
                log.warn("Invalid date format. Please use yyyy-MM-dd (e.g., 2025-10-27).");
            }

        } while (!valid);

        return date;
    }

    public static boolean isValidLogMessage(String message) {
        if (message == null || message.isEmpty()) {
            log.warn("Log message is invalid: cannot be null or empty.");
            return false;
        }

        return true;
    }

    public static String validateEncodingInput(Scanner scanner) {
        List<String> list = new ArrayList<>();
        list.add("UTF-8");
        list.add("ISO-8859-1");
        String encoding = "";
        do {
            log.info("Enter encoding (UTF-8 or ISO-8859-1): ");
            encoding = scanner.nextLine().trim();
            if (!list.contains(encoding)) {
                log.warn("Invalid encoding. Supported encodings: {}", String.join(", ", list));

            }
        } while (!list.contains(encoding));

        return encoding;
    }
}


