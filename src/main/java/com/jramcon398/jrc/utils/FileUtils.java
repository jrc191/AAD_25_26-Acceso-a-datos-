package com.jramcon398.jrc.utils;

import java.io.File;
import java.io.IOException;

public class FileUtils {
    public static void ensureFileExists(File file){
        try {
            if(!file.exists()){
                file.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("Error al crear el archivo: " + e.getMessage());
        }
    }
}
