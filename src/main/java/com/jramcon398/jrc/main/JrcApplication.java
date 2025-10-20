package com.jramcon398.jrc.main;

import com.jramcon398.jrc.datareaderwriter.CsvReader;
import com.jramcon398.jrc.utils.FileUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class JrcApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(JrcApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        File file = new File("alumnos.csv");
        FileUtils.ensureFileExists(file);
        CsvReader csvReader = new CsvReader(file);
        csvReader.readCsv();

    }
}
