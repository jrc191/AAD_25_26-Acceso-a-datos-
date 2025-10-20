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
 * Also provides methods to validate file existence and non-emptiness.
 */

@Slf4j
public class FileUtils {

    private static final String filePath = FileConstants.getFilePath();

    public static void ensureFileExists(File file) {
        try {
            if (!file.exists()) {
                boolean success = file.createNewFile();

                if (!success) {
                    log.warn("Could not create the file: {}", file.getPath());
                    return;
                }

                log.info("File created: {}", file.getPath());
                //Demo data
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

    public boolean existsAndIsFile(File file) {
        return file != null && file.exists() && file.isFile();
    }

}