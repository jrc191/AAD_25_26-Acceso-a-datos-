package com.jramcon398.jrc.utils;

public class InputValidator {

    public static boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isDouble(String input) {
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidGrade(float grade) {
        return grade >= 0 && grade <= 10;
    }

    public static boolean isNonEmptyString(String input) {
        //Devolvemos true si la cadena no es ni nula ni está vacía
        return input != null && !input.trim().isEmpty();
    }

}
