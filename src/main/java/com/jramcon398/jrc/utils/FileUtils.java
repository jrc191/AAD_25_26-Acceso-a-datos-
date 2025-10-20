package com.jramcon398.jrc.utils;

import com.jramcon398.jrc.models.Student;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Set;

/**
 * Class FileUtils: Utility class for file operations.
 * Ensures that a specified file exists, creating it if necessary.
 * Also provides methods to validate file existence and non-emptiness.
 * Includes functionality to check for duplicate student IDs.
 * Handles default CSV header and demo student data population.
 */

@Slf4j
public class FileUtils {
    
    // Default CSV header and demo student data
    private static final String DEFAULT_CSV_HEADER = FileConstants.getDEFAULT_CSV_HEADER();
    private static final List<Student> DEFAULT_STUDENTS = FileConstants.getDEFAULT_STUDENTS();

    public static boolean isIdDuplicated(Set<String> studentIds, String id) {
        return studentIds.contains(id);
    }


    public static void ensureCsvExists(File file) {
        try {
            if (!file.exists()) {
                boolean success = file.createNewFile();

                if (!success) {
                    log.warn("Could not create the file: {}", file.getPath());
                    return;
                }

                log.info("File created: {}", file.getPath());

                Files.writeString(file.toPath(), DEFAULT_CSV_HEADER, StandardCharsets.UTF_8);

                //Append mode so we don't overwrite the header
                for (Student student : DEFAULT_STUDENTS) {
                    String studentLine = student.getId() + "," + student.getName() + "," + student.getGrade() + "\n";
                    Files.writeString(
                            file.toPath(),
                            studentLine,
                            StandardCharsets.UTF_8,
                            StandardOpenOption.APPEND
                    );
                }

                log.info("Demo CSV data written to: {}", file.getPath());

            } else {
                log.info("File already exists: {}", file.getPath());

            }
        } catch (IOException e) {
            log.warn("Error reading file: {}", e.getMessage());
        }
    }

    public static void ensureEmptyFileExists(File file) {
        try {
            if (!file.exists()) {
                boolean success = file.createNewFile();
                if (success) {
                    log.info("File created: {}", file.getPath());
                } else {
                    log.warn("Could not create the file: {}", file.getPath());
                }
            }
            // Don't clear existing files - just ensure they exist
        } catch (IOException e) {
            log.warn("Error creating file: {}", e.getMessage());
        }
    }

    public static boolean validateFileNotEmpty(File file) {
        return file.length() > 0;
    }

    public static boolean existsAndIsFile(File file) {
        return file != null && file.exists() && file.isFile();
    }

    public static boolean csvValidations(File file) {
        return validateFileNotEmpty(file) && existsAndIsFile(file);
    }

}