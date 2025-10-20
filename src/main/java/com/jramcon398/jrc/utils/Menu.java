package com.jramcon398.jrc.utils;

import com.jramcon398.jrc.datamanipulation.CsvToJson;
import com.jramcon398.jrc.datamanipulation.CsvToXml;
import com.jramcon398.jrc.datareaderwriter.CsvReader;
import com.jramcon398.jrc.datareaderwriter.JsonWriter;
import com.jramcon398.jrc.datareaderwriter.XmlWriter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Scanner;

/**
 * Menu Class: Class responsible for displaying menu options and executing user-selected actions.
 * It utilizes CsvReader, CsvToJson, and CsvToXml classes for data operations.
 * Validation of user input is handled by InputValidator class.
 */

@Slf4j
public class Menu {

    File file = FileConstants.getFile();
    File fileJson = FileConstants.getFileJson();
    File fileXml = FileConstants.getFileXml();

    public Menu() {
        FileUtils.ensureFileExists(file);
        FileUtils.ensureFileExists(fileJson);
        FileUtils.ensureFileExists(fileXml);

    }

    public String getMenuOptions() {
        return """
                
                ===========================
                |       Menu Options:     |
                ===========================
                | 1. Read CSV File        |
                | 2. Convert CSV to JSON  |
                | 3. Convert CSV to XML   |
                | 4. Exit                 |
                ===========================
                | Select an option (1-4): |
                ===========================
                """;
    }

    public void executeMenu() {

        CsvReader csvReader = new CsvReader(file);
        JsonWriter jsonWriter = new JsonWriter();
        CsvToJson csvToJson = new CsvToJson(fileJson, jsonWriter);
        CsvToXml csvToXml = new CsvToXml(FileConstants.getFileXml(), new XmlWriter());

        Scanner scanner = new Scanner(System.in);
        boolean cancel = false;

        do {
            log.info(getMenuOptions());
            log.info("Enter the desired option (1-4): ");
            String input = scanner.next();

            int op;
            try {
                op = InputValidator.parseIntInRange(input, 1, 4);

                switch (op) {
                    case 1 -> readCsv(csvReader);
                    case 2 -> convertCsvToJson(csvToJson);
                    case 3 -> convertCsvToXml(csvToXml);
                    case 4 -> {
                        log.info("Exiting...");
                        cancel = true;
                    }
                }

            } catch (IllegalArgumentException e) {
                log.warn("Invalid input: {}. Try again.", e.getMessage());
            }

        } while (!cancel);

        scanner.close();


    }


    private void readCsv(CsvReader reader) {
        log.info("CSV Content:\n{}", reader.readCsv());
    }
    
    private void convertCsvToJson(CsvToJson converter) {
        if (converter.csvToJson(file)) {
            log.info("CSV converted to JSON at {}", fileJson.getAbsolutePath());
        } else {
            log.error("Error converting CSV to JSON");
        }
    }

    private void convertCsvToXml(CsvToXml converter) {
        if (converter.csvToXml(file)) {
            log.info("CSV converted to XML at {}", fileXml.getAbsolutePath());
        } else {
            log.error("Error converting CSV to XML");
        }
    }


}
