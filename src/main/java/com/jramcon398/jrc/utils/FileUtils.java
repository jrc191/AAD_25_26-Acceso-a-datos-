package com.jramcon398.jrc.utils;

import com.jramcon398.jrc.models.Student;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Class FileUtils: Utility class for file operations.
 * Ensures that a specified file exists, creating it if necessary.
 */

@Slf4j
public class FileUtils {

    private static String filePath = FileConstants.filePath;

    public static void ensureFileExists(File file) {
        try {
            if (!file.exists()) {
                file.createNewFile();

                Student student1 = new Student(1, "Ana", 8.5f);
                Student student2 = new Student(2, "Juan", 6.7f);
                Student student3 = new Student(3, "Luis", 9.0f);

                Files.writeString(Path.of(filePath), "id,nombre,nota\n" +
                        student1.getId() + "," + student1.getName() + "," + student1.getGrade() + "\n" +
                        student2.getId() + "," + student2.getName() + "," + student2.getGrade() + "\n" +
                        student3.getId() + "," + student3.getName() + "," + student3.getGrade() + "\n", StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            log.warn("Error reading file: {}", e.getMessage());
        }
    }

    public boolean validateFileNotEmpty(File file) {
        return file.length() > 0;
    }
}