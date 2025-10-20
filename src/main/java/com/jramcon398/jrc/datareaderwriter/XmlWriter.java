package com.jramcon398.jrc.datareaderwriter;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.jramcon398.jrc.models.Student;
import com.jramcon398.jrc.models.Students;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * XmlWriter Class: Class responsible for writing a list of Student objects to an XML file.
 * Utilizes Jackson's XmlMapper for XML serialization.
 * Uses Students wrapper class to encapsulate the list of Student objects.
 * WRAP_ROOT_VALUE is set to false to avoid additional root element.
 * Enables INDENT_OUTPUT for pretty-printing the XML output.
 */

@Slf4j
public class XmlWriter {

    private final XmlMapper xmlMapper;

    public XmlWriter() {
        xmlMapper = new XmlMapper();
        xmlMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public boolean writeXml(File file, List<Student> data) {
        // If the file exists, delete it to avoid appending to existing data
        try {
            if (file.exists()) {
                boolean delete = file.delete();
                if (!delete) {
                    log.error("Error deleting existing XML file: {}", file.getAbsolutePath());
                    return false;
                }
            }

            Students students = new Students(data);
            xmlMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValue(file, students);
            return true;
        } catch (IOException e) {
            log.error("Error writing XML: {}", e.getMessage());
            return false;
        }
    }

}
