package com.jramcon398.jrc.datareaderwriter;

import com.jramcon398.jrc.models.Student;
import com.jramcon398.jrc.utils.FileUtils;
import com.jramcon398.jrc.utils.InputValidator;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * CsvParser Class: Responsible for parsing CSV data and creating Student objects.
 * Refactored to delegate CSV reading to CsvReader class, used in both CsvToJson and CsvToXml classes.
 * Validation of input data is handled by InputValidator class.
 * Validation for duplicate IDs is handled by FileUtils class.
 */

@Slf4j
public class CsvParser {

    private final CsvReader csvReader;

    public CsvParser(CsvReader csvReader) {
        this.csvReader = csvReader;
    }

    /**
     * parseStudents Method:
     * Parses CSV data to create a list of Student objects.
     * Validates each line for correct format, duplicate IDs, and grade ranges.
     *
     * @return List<Student> (list of valid Student objects)
     */
    public List<Student> parseStudents() {
        List<Student> students = new ArrayList<>();
        Set<String> usedIds = new HashSet<>();
        String studentData = csvReader.readCsv();

        // Check for error messages from CsvReader. Errors are defined on CsvReader class.
        if (studentData == null || studentData.equals("File not found.") || studentData.equals("Empty File.")) {
            log.warn("Cannot parse students: {}", studentData);
            return students;
        }

        String[] lines = studentData.split("\n");

        int position = 0;
        for (String line : lines) {
            if (line.trim().isEmpty()) {
                continue;
            }

            // Split line by commas, each column represents a field
            String[] data = line.split(",");

            // Validate format length. Max 3 columns expected.
            if (InputValidator.isValidCsvFormat(data, line)) {
                log.warn("Invalid CSV format at line {}: '{}'. Expected 3 columns, found {}", position + 1, line, data.length);
                continue;
            }

            try {
                String idStr = data[0].trim();
                int id = Integer.parseInt(idStr);
                String name = data[1].trim();
                String gradeStr = data[2].trim();

                if (FileUtils.isIdDuplicated(usedIds, idStr)) {
                    log.warn("Duplicate ID found at line {}: ID {} already exists. Skipping student: {}",
                            position + 1, id, name);
                    continue;
                }

                Double gradeDouble = InputValidator.parseGrade(gradeStr, idStr);
                if (gradeDouble == null) {
                    continue;
                }

                float grade = gradeDouble.floatValue();

                if (!InputValidator.isGradeInRange(grade, idStr)) {
                    log.warn("Expected grade to be in range 0-10. Result on line -> {}", line);
                    continue;
                }

                students.add(new Student(id, name, grade));
                usedIds.add(idStr); // Track used IDs to prevent duplicates
            } catch (NumberFormatException e) {
                log.warn("Invalid number format at line {}: '{}'", position + 1, line);
            }

            position++;
        }

        return students;
    }

}
