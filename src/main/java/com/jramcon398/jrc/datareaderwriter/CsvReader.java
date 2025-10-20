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
 */

@Slf4j
public class CsvReader {

    File file;
    Path filePath = FileConstants.getFile().toPath();

    public CsvReader(File file) {
        this.file = file;
    }


    public String readCsv() {

        if (!validateFile()) {
            return "File validation failed.";
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

            String line;
            String content = "";
            boolean firstLine = true;
            Set<String> studentIds = new HashSet<>();
            boolean hasInvalidGrades = false;

            //We skip the first line (header).
            while ((line = br.readLine()) != null) {

                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] fields = line.split(",");

                // Validate format length. Max 3 columns expected.
                if (InputValidator.isValidCsvFormat(fields, line)) {
                    continue;
                }

                String id = fields[0].trim();
                String gradeStr = fields[2].trim();

                // Check for duplicated IDs
                if (FileUtils.isIdDuplicated(studentIds, id)) {
                    log.warn("Duplicated ID found: ID {} already exists. Skipping Record: {}",
                            id, line);
                    continue;
                }

                // Validate grade format
                if (InputValidator.parseGrade(gradeStr, id) == null) {
                    continue;
                }

                //Grade, if valid format, is not a factor for skipping the line (only on reading), just a warning is logged (because out of range).
                if (!InputValidator.isValidGrade(gradeStr, id)) {
                    log.warn("Expected grade to be in range 0-10. Result on line -> {}", line);
                    hasInvalidGrades = true;
                }

                studentIds.add(id);

                content += line + "\n";

            }

            if (hasInvalidGrades) {
                log.warn("CSV reading completed with invalid grade warnings.");
            }

            return content;
        } catch (IOException e) {
            log.warn("Error during CSV reading {}", e.getMessage());
            return "Reading error.";
        }


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
