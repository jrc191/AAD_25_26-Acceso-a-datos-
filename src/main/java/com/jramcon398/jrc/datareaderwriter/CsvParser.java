package com.jramcon398.jrc.datareaderwriter;

import com.jramcon398.jrc.models.Student;

import java.util.ArrayList;
import java.util.List;


/**
 * CsvParser Class: Responsible for parsing CSV data and creating Student objects.
 * Refactored to delegate CSV reading to CsvReader class, used in both CsvToJson and CsvToXml classes.
 */

public class CsvParser {

    private final CsvReader csvReader;

    public CsvParser(CsvReader csvReader) {
        this.csvReader = csvReader;
    }

    //We parse the CSV data and create Student objects. CSV reading is delegated to CsvReader class.
    public List<Student> parseStudents() {
        List<Student> students = new ArrayList<>();
        String studentData = csvReader.readCsv();
        String[] lines = studentData.split("\n");

        for (String line : lines) {
            if (line.trim().isEmpty()) {
                continue;
            }
            String[] datos = line.split(",");
            int id = Integer.parseInt(datos[0]);
            String name = datos[1];
            float grade = Float.parseFloat(datos[2]);

            students.add(new Student(id, name, grade));
        }

        return students;
    }

}
