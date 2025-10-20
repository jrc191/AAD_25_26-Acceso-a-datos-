package com.jramcon398.jrc.datareaderwriter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jramcon398.jrc.models.Student;
import com.jramcon398.jrc.models.Students;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * JsonWriter Class: Class responsible for writing a list of Student objects to a JSON file.
 * Utilizes Jackson's ObjectMapper for JSON serialization.
 * Handles file existence by deleting existing files before writing new data.
 */

@Slf4j
public class JsonWriter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public boolean writeJson(File file, List<Student> data) {
        // If the file exists, delete it to avoid appending to existing data
        if (file.exists()) {
            boolean delete = file.delete();
            if (!delete) {
                log.error("Error deleting existing JSON file: {}", file.getAbsolutePath());
                return false;
            }
        }

        Students students = new Students(data);

        try {
            objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValue(file, students);
            return true;
        } catch (IOException e) {
            log.error("Error writing JSON: {}", e.getMessage());
            return false;
        }
    }

}
