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

    public static boolean isValidCsvFormat(String[] fields, String line) {
        if (fields.length < 3) {
            log.warn("Invalid CSV format: '{}'. Expected at least 3 columns.", line);
            return true;
        }
        return false;
    }

    public static Double parseGrade(String gradeStr, String id) {
        try {
            return Double.parseDouble(gradeStr);
        } catch (NumberFormatException e) {
            log.error("Invalid grade format for student {}: {}. Skipping record.", id, gradeStr);
            return null;
        }
    }

    public static boolean isGradeInRange(double grade, String id) {
        if (grade < 0 || grade > 10) {
            log.warn("Invalid grade found for student (FORMAT 0-10) {}: {}. Keeping record.", id, grade);
            return false;
        }
        return true;
    }

    public static boolean isValidGrade(String gradeStr, String id) {
        Double grade = parseGrade(gradeStr, id);
        if (grade == null) {
            return false;
        }
        return isGradeInRange(grade, id);
    }

}
