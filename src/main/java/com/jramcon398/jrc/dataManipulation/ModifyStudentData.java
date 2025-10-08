package com.jramcon398.jrc.dataManipulation;

import com.jramcon398.jrc.utils.InputValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;


/// Clase ModifyStudentData: Clase para modificar la nota de un alumno en una posición específica
///                          Las clases contenidas en dataManipulation se encargan de las operaciones
///                          de inserción, consulta y modificación de los registros de alumnos.
///                          (Utilizan la clase DataRW para las operaciones de lectura y escritura
///                          en el archivo binario.)
///
///                          Utiliza la clase GetStudentDataByPosition para obtener la posición
///                          del alumno a modificar.

@SpringBootApplication
@Slf4j

public class ModifyStudentData {

    private static final int NAME_LENGTH = 20;
    private static final int RECORD_SIZE = 4 + NAME_LENGTH * 2 + 4; // 4 bytes por id + 20 caracteres * 2 bytes/caracteres + 4 bytes por integer (nota) = 48

    public ModifyStudentData() {

    }

    public void modifyStudentGrade(File file) {
        GetStudentDataByPosition studentDataByPosition = new GetStudentDataByPosition();
        int pos = studentDataByPosition.getStudentInfoByPosition(file);

        if (pos == -1) {
            log.warn("No se pudo modificar la nota debido a un error en la posición.");
            return;
        }

        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            // Nos movemos al inicio del registro deseado
            raf.seek(pos * RECORD_SIZE);

            // Leemos el ID y el nombre para mantenerlos
            int id = raf.readInt();
            char[] nameChars = new char[NAME_LENGTH];
            for (int i = 0; i < NAME_LENGTH; i++) {
                nameChars[i] = raf.readChar();
            }

            String name = new String(nameChars).trim();
            Scanner scanner = new Scanner(System.in);
            String input;
            float grade = -1;

            //Validación de la nueva nota
            do {
                log.info("Ingrese la nueva nota (0-10): ");
                input = scanner.next();
                if (InputValidator.isDouble(input)) {
                    grade = Float.parseFloat(input);
                    if (InputValidator.isValidGrade(grade)) {
                        break;
                    } else {
                        log.warn("Nota inválida. Debe estar entre 0 y 10.");
                    }
                } else {
                    log.warn("Entrada inválida. Por favor, ingrese un número válido.");
                }
            } while (true);

            // Ahora escribimos la nueva nota en la posición correcta
            raf.writeFloat(grade);
            log.warn("------------------------------------------------------------------------------");
            log.info("Alumno leído [{}]: ID: {}, Nombre: {}, Nota: {}", pos, id, name, grade);
            log.info("Nota del alumno en posición {} modificada exitosamente a {}.", pos, grade);
            log.warn("------------------------------------------------------------------------------");

        } catch (IOException e) {
            log.error("Error durante la modificación del archivo: {} ", e.getMessage());
        }




    }

}
