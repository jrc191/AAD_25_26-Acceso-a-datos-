package com.jramcon398.jrc.datamanipulation;

import com.jramcon398.jrc.datareaderwriter.CsvParser;
import com.jramcon398.jrc.datareaderwriter.CsvReader;
import com.jramcon398.jrc.datareaderwriter.JsonWriter;
import com.jramcon398.jrc.models.Student;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;

/**
 * CsvToJson Class: Class responsible for converting CSV data to JSON format.
 * Writing operation is delegated to JsonWriter class.
 */

@Slf4j
public class CsvToJson {

    private final File fileJson;
    private final JsonWriter jsonWriter;

    public CsvToJson(File fileJson, JsonWriter jsonWriter) {
        this.fileJson = fileJson;
        this.jsonWriter = jsonWriter;
    }

    //We get the data from csv file, we parse it, and we create Student objects
    //Writing operation is delegated to JsonWriter class
    public boolean csvToJson(File fileCSV) {
        CsvParser csvParser = new CsvParser(new CsvReader(fileCSV));
        List<Student> students = csvParser.parseStudents();
        return jsonWriter.writeJson(fileJson, students);

    }
}
