package com.jramcon398.jrc.utils;

import lombok.Getter;
import lombok.Setter;

import java.io.File;

/**
 * FileConstants Class: Class responsible
 * for defining file-related constants used across the application.
 */

public class FileConstants {

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
