package com.jramcon398.jrc.utils;

import java.io.File;
import java.io.IOException;

/// Class FileUtils: Utility class for file operations.
///                  Ensures that a specified file exists, creating it if necessary.

public class FileUtils {
    public static void ensureFileExists(File file){
        try {
            if(!file.exists()){
                file.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}
