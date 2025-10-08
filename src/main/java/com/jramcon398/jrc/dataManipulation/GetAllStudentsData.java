package com.jramcon398.jrc.dataManipulation;

import com.jramcon398.jrc.dataReaderWriter.DataRW;

import java.io.File;

/// Clase GetAllStudentsData: Clase para obtener todos los registros de alumnos
///                           Las clases contenidas en dataManipulation se encargan de las operaciones
///                           de inserción, consulta y modificación de los registros de alumnos.
///                           (Utilizan la clase DataRW para las operaciones de lectura y escritura
///                           en el archivo binario.)


public class GetAllStudentsData {

    DataRW dataRW = new DataRW();
    public GetAllStudentsData() {

    }

    public void getAllStudentData(File file) {
        dataRW.readFileObject(file);
    }
}
