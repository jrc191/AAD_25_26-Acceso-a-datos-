package com.jramcon398.jrc.datareaderwriter;

import com.jramcon398.jrc.utils.FileConstants;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class CsvReader {

    File file = FileConstants.file;

    public CsvReader(File file) {
        this.file = file;
    }

    public void readCsv() {

        if (file.exists()) {
            log.info("El archivo CSV existe: {}", file.getPath());

        } else {
            log.warn("El archivo CSV no existe: {}", file.getPath());
            
        }

    }

}
