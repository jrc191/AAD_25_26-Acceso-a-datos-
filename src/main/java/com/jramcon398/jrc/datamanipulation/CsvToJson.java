package com.jramcon398.jrc.datamanipulation;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jramcon398.jrc.datareaderwriter.CsvReader;
import com.jramcon398.jrc.models.Student;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CsvToJson {

    private final File fileJson;

    public CsvToJson(File fileJson) {

        if (fileJson.exists()) {
            log.info("The JSON file already exists.");
            this.fileJson = fileJson;
        } else {
            log.warn("The JSON file does not exist. Creating a new one.");
            this.fileJson = new File("students.json");
        }


    }

    public boolean writeJsonFile(File file) {

        try {
            //Splitting CSV data and mapping to Student objects to write to JSON
            //Each line is separated by \n, and each field by commas
            ObjectMapper mapeador = new ObjectMapper();
            CsvReader csvReader = new CsvReader(file);
            String studentData = csvReader.readCsv();
            String[] lineas = studentData.split("\n");
            List<Student> students = new ArrayList<>();

            log.info("Student Data: " + studentData);

            for (String linea : lineas) {

                //If the line is empty, skip it, in case there are blank lines at the end of the CSV
                if (linea.trim().isEmpty()) {
                    continue;
                }

                //Split data by commas, and parse each field
                String[] datos = linea.split(",");
                int id = Integer.parseInt(datos[0]);
                String name = datos[1];
                float grade = Float.parseFloat(datos[2]);

                Student student = new Student(id, name, grade);
                log.info("Student Object: " + student.toString());

                students.add(new Student(id, name, grade));

            }

            mapeador.writeValue(fileJson, students);

            //Student student = new Student(id, name, grade);
            //mapeador.writeValue(new File("students.json"), student);

        } catch (StreamWriteException e) {
            log.warn(e.getMessage());

        } catch (DatabindException e) {
            log.warn(e.getMessage());

        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return true;
    }
}
