package com.jramcon398.jrc.datareaderwriter;

import com.jramcon398.jrc.utils.FileConstants;
import com.jramcon398.jrc.utils.FileUtils;
import com.jramcon398.jrc.utils.InputValidator;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

/**
 * CsvReader class: Class responsible for reading CSV files.
 * It checks for file existence and emptiness before reading its content.
 * Validates each line for correct format, duplicate IDs, and grade ranges.
 */

@Slf4j
public class CsvReader {

    File file;
    Path filePath = FileConstants.getFile().toPath();

    public CsvReader(File file) {
        this.file = file;
    }

    /**
     * readCsv Method:
     * Reads the CSV file and returns its content as a string.
     * Validates file existence, emptiness, line format, duplicate IDs, and grade ranges
     *
     * @return String (content of the CSV file or error message)
     */

    public String readCsv() {
        if (!validateFile()) {
            return "File validation failed.";
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

            return processCsv(br);

        } catch (IOException e) {
            log.warn("Error during CSV reading {}", e.getMessage());
            return "Reading error.";
        }
    }

    /**
     * processCsv Method:
     * Process the CSV content from BufferedReader.
     * Validates each line for format, duplicate IDs, and grade ranges.
     * Returns the valid CSV content as a String.
     *
     * @return String (valid CSV content)
     */
    private String processCsv(BufferedReader br) throws IOException {
        String content = "";
        Set<String> studentIds = new HashSet<>();
        boolean hasInvalidGrades = false;
        boolean firstLine = true;
        String line;

        while ((line = br.readLine()) != null) {
            // Skip header and empty lines
            if (firstLine) {
                firstLine = false;
                continue;
            }

            if (line.trim().isEmpty()) {
                continue;
            }

            String[] fields = line.split(",");

            // Validate format length
            if (InputValidator.isValidCsvFormat(fields, line)) {
                continue;
            }

            String id = fields[0].trim();
            String gradeStr = fields[2].trim();

            // Check for duplicated IDs
            if (FileUtils.isIdDuplicated(studentIds, id)) {
                log.error("Duplicated ID found: ID {} already exists. Skipping Record: {}", id, line);
                continue;
            }

            // Validate grade format
            if (InputValidator.parseGrade(gradeStr, id) == null) {
                continue;
            }

            // Check grade range
            if (!InputValidator.isValidGrade(gradeStr, id)) {
                log.warn("Expected grade to be in range 0-10. Result on line -> {}", line);
            }

            studentIds.add(id);
            content += line + "\n";
        }


        return content;
    }

    private boolean validateFile() {
        if (!FileUtils.existsAndIsFile(file)) {
            log.warn("CSV File does not exist: {}", filePath.toString());
            return false;

        }
        if (!FileUtils.validateFileNotEmpty(file)) {
            log.warn("File is empty: {}", file.getPath());
            return false;
        }
        return true;
    }

}
