package com.jramcon398.jrc.datamanipulation;

import com.jramcon398.jrc.datareaderwriter.CsvParser;
import com.jramcon398.jrc.datareaderwriter.CsvReader;
import com.jramcon398.jrc.datareaderwriter.XmlWriter;
import com.jramcon398.jrc.models.Student;

import java.io.File;
import java.util.List;

/**
 * CsvToXml Class: Class responsible for converting CSV data to XML format.
 * Writing operation is delegated to XmlWriter class.
 */

public class CsvToXml {

    private final File fileXml;
    private final XmlWriter xmlWriter;

    public CsvToXml(File file, XmlWriter xmlWriter) {
        this.fileXml = file;
        this.xmlWriter = xmlWriter;
    }

    //We get the data from csv file, we parse it, and we create Student objects
    //Writing operation is delegated to XmlWriter class
    public boolean csvToXml(File file) {

        CsvParser csvParser = new CsvParser(new CsvReader(file));
        List<Student> students = csvParser.parseStudents();
        return xmlWriter.writeXml(fileXml, students);
    }
}
