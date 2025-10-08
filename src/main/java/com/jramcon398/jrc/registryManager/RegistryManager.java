package com.jramcon398.jrc.registryManager;

import com.jramcon398.jrc.dataManipulation.GetAllStudentsData;
import com.jramcon398.jrc.dataManipulation.GetStudentDataByPosition;
import com.jramcon398.jrc.dataManipulation.InsertStudentData;
import com.jramcon398.jrc.dataManipulation.ModifyStudentData;
import com.jramcon398.jrc.dataReaderWriter.DataRW;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.Scanner;

/// Clase RegistryManager: esta clase se utiliza para poder ejecutar las operaciones
///                        de Inserción, consulta o modificación de estudiantes mediante
///                        un menú.
///                        Implementa las clases de dataManipulation para realizar las
///                        operaciones.


@SpringBootApplication
@Slf4j
public class RegistryManager {

    public RegistryManager() {
        // Para poder llamar a los métodos desde aquí
    }

    public void menuText(){
        log.info("---------- MENÚ DE OPCIONES ----------");
        log.info("1. Leer archivo de estudiantes");
        log.info("2. Insertar nuevo estudiante");
        log.info("3. Modificar nota de estudiante");
        log.info("4. Consultar estudiante por posición");
        log.info("5. Salir");
        log.info("--------------------------------------");
    }

    public void menu(File file){

        Scanner scanner = new Scanner(System.in);
        int op=-1;
        boolean cancel=false;

        do {
            menuText();
            log.info("Ingrese la opción deseada (escriba 5 para salir): ");
            String input = scanner.next();

            try {
                //Probamos a parsear a float la nota
                op = Integer.parseInt(input);

                switch (op) {
                    case 1:
                        log.info("Opción 1 seleccionada: Leer archivo de estudiantes");
                        GetAllStudentsData studentsData = new GetAllStudentsData();
                        studentsData.getAllStudentData(file);
                        break;
                    case 2:
                        log.info("Opción 2 seleccionada: Insertar nuevo estudiante");
                        InsertStudentData insertStudentData = new InsertStudentData();
                        insertStudentData.insertStudentData(file);
                        break;
                    case 3:
                        log.info("Opción 3 seleccionada: Modificar nota de estudiante");
                        studentsData = new GetAllStudentsData();
                        studentsData.getAllStudentData(file);
                        ModifyStudentData modifyStudentData = new ModifyStudentData();
                        modifyStudentData.modifyStudentGrade(file); // Aquí se pueden pedir los parámetros al usuario
                        break;
                    case 4:
                        log.info("Opción 4 seleccionada: Consultar estudiante por posición");
                        GetStudentDataByPosition studentDataByPosition = new GetStudentDataByPosition();
                        studentDataByPosition.getStudentInfoByPosition(file);

                        break;
                    case 5:
                        log.info("Saliendo del programa...");
                        cancel=true;
                        break;
                    default:
                        log.warn("Opción inválida. Por favor, ingrese un número entre 1 y 5.");
                        break;
                }

            } catch (NumberFormatException e) {
                log.warn("Entrada inválida. Debe ser un número o 'EXIT'. Inténtelo de nuevo.");
            }

        } while (!cancel || op<1 || op>5);

    }


}
