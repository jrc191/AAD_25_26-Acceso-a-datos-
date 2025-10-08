package com.jramcon398.jrc.dataManipulation;

import com.jramcon398.jrc.dataReaderWriter.DataRW;
import com.jramcon398.jrc.models.Alumno;
import com.jramcon398.jrc.utils.InputValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.Scanner;


/// Clase InsertStudentData: Clase para insertar un alumno de manera secuencial
///                          Las clases contenidas en dataManipulation se encargan de las operaciones
///                          de inserción, consulta y modificación de los registros de alumnos.
///                          (Utilizan la clase DataRW para las operaciones de lectura y escritura
///                          en el archivo binario.)

@SpringBootApplication
@Slf4j
public class InsertStudentData {

    DataRW dataRW = new DataRW();

    public void insertStudentData(File file) {
        Scanner scanner = new Scanner(System.in);
        int id = -1;
        String name="";
        float grade = -1;
        String fullContent = "";

        //Validaciones
        do {
            log.info("Ingrese el id del alumno: FORMATO numérico>0 (escriba EXIT para salir): ");
            String input = scanner.next();

            if (input.equals("EXIT")) {
                break;
            }

            if (InputValidator.isInteger(input)) {
                id = Integer.parseInt(input);
                if (id > 0) {
                    if (dataRW.idExists(file, id)) {
                        log.warn("El id ya existe. Introduzca uno diferente.");
                    } else {
                        fullContent += id + ";";
                        break;
                    }
                } else {
                    log.warn("El id debe de ser mayor que cero.");
                }
            } else {
                log.warn("Entrada inválida. Debe ser un número o 'EXIT'. Inténtelo de nuevo.");
            }
        } while (true);

        do {
            log.info("Ingrese el nombre del alumno: (20 caracteres máximo) (escriba EXIT para salir): ");
            String input = scanner.next();

            if (input.equals("EXIT")) {
                break;
            }

            if (InputValidator.isNonEmptyString(input) && input.length() <= 20) {
                name = input;
                fullContent += name + ";";
                break;
            } else {
                log.warn("El nombre debe tener 20 caracteres máximo y no estar vacío.");
            }
        } while (true);

        do {
            log.info("Ingrese la nota del alumno: FORMATO 0-10 (escriba EXIT para salir): ");
            String input = scanner.next();

            if (input.equals("EXIT")) {
                break;
            }

            if (InputValidator.isDouble(input)) {
                grade = Float.parseFloat(input);
                if (InputValidator.isValidGrade(grade)) {
                    fullContent += grade + ";";
                    break;
                } else {
                    log.warn("La nota debe estar entre 0 y 10.");
                }
            } else {
                log.warn("Entrada inválida. Debe ser un número o 'EXIT'. Inténtelo de nuevo.");
            }
        } while (true);


        log.info("\nTexto:\n{}", fullContent);

        // Escritura de alumno en el archivo
        Alumno alumno = new Alumno(id, name, grade);
        log.info("Alumno creado: ID={}, Nombre={}, Nota={}", alumno.getId(), alumno.getNombre(), alumno.getNota());
        dataRW.writeFileObject(file, alumno);

    }

}
