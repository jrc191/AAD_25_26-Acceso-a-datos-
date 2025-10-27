package com.jramcon398.jrc.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Slf4j
@UtilityClass
public class InputValidation {

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
                log.warn("Invalid encoding. Supported encodings: UTF-8, ISO-8859-1");
            }
        } while (!list.contains(encoding));

        return encoding;
    }
}


