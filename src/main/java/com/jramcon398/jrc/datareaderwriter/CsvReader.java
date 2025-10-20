package com.jramcon398.jrc.datareaderwriter;

import com.jramcon398.jrc.utils.FileConstants;
import com.jramcon398.jrc.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

@Slf4j
public class CsvReader {

    File file = FileConstants.file;
    FileUtils fileUtils = new FileUtils();

    public CsvReader(File file) {
        this.file = file;
    }

    public String readCsv() {

        if (file.exists() && file.isFile()) {
            log.info("El archivo CSV existe: {}", file.getPath());

        } else {
            log.warn("El archivo CSV no existe: {}", file.getPath());
            return "El archivo CSV no existe.";
        }

        if (fileUtils.validateFileNotEmpty(file)) {
            try {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(new FileInputStream(file.getPath()), "UTF-8"));

                String linea;
                String contenido = "";
                int aux = 0;
                while ((linea = br.readLine()) != null) {
                    if (aux == 0) {
                        aux++;
                        continue; // Saltar la primera línea (encabezado)
                    } else {
                        log.info("Línea leída del archivo CSV: {}", linea);
                        contenido += linea + "\n";
                    }

                }
                br.close();
                log.info("\nContenido del archivo CSV:\n{}", contenido);
                return contenido;
            } catch (IOException e) {
                log.warn("Ocurrió una excepción al leer el fichero {}", e.getMessage());
                return "Ocurrió una excepción al leer el fichero.";
            }
        } else {
            log.warn("El archivo está vacío: {}", file.getPath());
            return "El archivo está vacío.";
        }

    }

}
