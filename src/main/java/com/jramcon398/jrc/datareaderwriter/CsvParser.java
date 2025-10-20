package com.jramcon398.jrc.datareaderwriter;

import com.jramcon398.jrc.models.Student;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;


/**
 * CsvParser Class: Responsible for parsing CSV data and creating Student objects.
 * Refactored to delegate CSV reading to CsvReader class, used in both CsvToJson and CsvToXml classes.
 */

@Slf4j
public class CsvParser {

    private final CsvReader csvReader;

    public CsvParser(CsvReader csvReader) {
        this.csvReader = csvReader;
    }

    //We parse the CSV data and create Student objects. CSV reading is delegated to CsvReader class.
    public List<Student> parseStudents() {
        List<Student> students = new ArrayList<>();
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

            // Validate that there are exactly 3 columns (id, name, grade)
            if (data.length < 3) {
                log.warn("Invalid CSV format at line {}: '{}'. Expected 3 columns, found {}", position + 1, line, data.length);
                continue;
            }
            
            try {
                int id = Integer.parseInt(data[0].trim());
                String name = data[1].trim();
                float grade = Float.parseFloat(data[2].trim());

                students.add(new Student(id, name, grade));
            } catch (NumberFormatException e) {
                log.warn("Invalid number format at line {}: '{}'", position + 1, line);
            }

            position++;
        }

        return students;
    }

}
