package com.jramcon398.jrc.dataManipulation;

import com.jramcon398.jrc.dataReaderWriter.DataRW;

import java.io.File;

/// Class GetAllStudentsData: Class to obtain all student records
///                           The classes contained in dataManipulation are responsible for operations
///                           insertion, consultation and modification of student records.
///                           (They use the DataRW class for read and write operations
///                           in the binary file.)


public class GetAllStudentsData {

    DataRW dataRW = new DataRW();
    public GetAllStudentsData() {

    }

    public void getAllStudentData(File file) {
        dataRW.readFileObject(file);
    }
}
