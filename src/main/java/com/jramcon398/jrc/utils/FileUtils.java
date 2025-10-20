package com.jramcon398.jrc.utils;

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

                Files.writeString(Path.of(filePath), "id,nombre,nota\n" +
                        "1,Ana,8.5\n" +
                        "2,Juan,6.7\n" +
                        "3,Luis,9.0", StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            log.warn("Error reading file: {}", e.getMessage());
        }
    }
}