package com.jramcon398.jrc.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * InputValidator Class: Utility class for validating user inputs.
 */

@Slf4j
public class InputValidator {

    public static int parseIntInRange(String input, int min, int max) throws IllegalArgumentException {
        int value;
        try {
            value = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            log.error("Input is not a valid integer.");
            return -1;
        }

        if (value < min || value > max) {
            log.error("Input must be between " + min + " and " + max + ".");
            return -1;
        }

        return value;
    }
    
}
