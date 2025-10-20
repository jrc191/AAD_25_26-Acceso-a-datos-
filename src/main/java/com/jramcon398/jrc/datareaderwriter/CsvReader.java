package com.jramcon398.jrc.datareaderwriter;

import com.jramcon398.jrc.utils.FileConstants;
import com.jramcon398.jrc.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

/**
 * CsvReader class: Class responsible for reading CSV files.
 * It checks for file existence and emptiness before reading its content.
 */

@Slf4j
public class CsvReader {

    File file;
    Path filePath = FileConstants.getFile().toPath();
    FileUtils fileUtils = new FileUtils();

    public CsvReader(File file) {
        this.file = file;
    }


    public String readCsv() {

        if (!fileUtils.existsAndIsFile(file)) {
            log.warn("CSV File does not exist: {}", filePath.toString());
            return "File not found.";

        }
        if (!fileUtils.validateFileNotEmpty(file)) {
            log.warn("File is empty: {}", file.getPath());
            return "Empty File.";
        }


        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));

            String linea;
            String contenido = "";
            int aux = 0;

            //We skip the first line (header)
            while ((linea = br.readLine()) != null) {
                if (aux == 0) {
                    aux++;
                } else {
                    contenido += linea + "\n";
                }

            }
            br.close();
            return contenido;
        } catch (IOException e) {
            log.warn("Error during CSV reading {}", e.getMessage());
            return "Reading error.";
        }


    }

}
