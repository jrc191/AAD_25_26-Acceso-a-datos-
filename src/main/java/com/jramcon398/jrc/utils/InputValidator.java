package com.jramcon398.jrc.utils;

/// Class InputValidator: Utility class for validating user inputs.
/// Provides methods to check if a string can be parsed as an integer or float,
/// if a grade is within a valid range, and if a string is non-empty.


public class InputValidator {

    public static boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isFloat(String input) {
        try {
            Float.parseFloat(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidGrade(float grade) {
        return grade >= 0 && grade <= 10;
    }

    public static boolean isNonEmptyString(String input) {
        //Return true if the string is not null and not empty after trimming whitespace
        return input != null && !input.trim().isEmpty();
    }

}
