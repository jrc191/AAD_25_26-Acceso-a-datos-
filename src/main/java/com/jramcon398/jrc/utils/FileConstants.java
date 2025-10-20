package com.jramcon398.jrc.utils;

import com.jramcon398.jrc.models.Student;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.List;

/**
 * FileConstants Class: Class responsible
 * for defining file-related constants used across the application.
 * Includes default CSV header, sample student data,
 * and file paths for CSV, JSON, and XML files.
 */

public class FileConstants {

    @Getter
    private static final String DEFAULT_CSV_HEADER = "id,nombre,nota\n";
    @Getter
    private static final List<Student> DEFAULT_STUDENTS = List.of(
            new Student(1, "Ana", 8.5f),
            new Student(2, "Juan", 6.7f),
            new Student(3, "Luis", 9.0f)
    );

    @Getter
    @Setter
    private static File file = new File("alumnos.csv");
    @Getter
    @Setter
    private static String filePath = getFile().getPath();
    @Getter
    @Setter
    private static File fileJson = new File("students.json");
    @Getter
    @Setter
    private static File fileXml = new File("students.xml");

    private FileConstants() {
    }


}
