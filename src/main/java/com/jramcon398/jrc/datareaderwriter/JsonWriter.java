package com.jramcon398.jrc.datareaderwriter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jramcon398.jrc.models.Student;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
public class JsonWriter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public boolean writeJson(File file, List<Student> data) {
        try {
            objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValue(file, data);
            return true;
        } catch (IOException e) {
            log.error("Error writing JSON: {}", e.getMessage());
            return false;
        }
    }

}
