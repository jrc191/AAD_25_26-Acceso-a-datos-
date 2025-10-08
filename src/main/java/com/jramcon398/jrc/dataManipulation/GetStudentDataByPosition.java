package com.jramcon398.jrc.dataManipulation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;


/// Clase GetStudentDataByPosition: Clase para consultar la información de un alumno en una posición específica
///                                 Las clases contenidas en dataManipulation se encargan de las operaciones
///                                 de inserción, consulta y modificación de los registros de alumnos.
///                                 (Utilizan la clase DataRW para las operaciones de lectura y escritura
///                                 en el archivo binario.)

@SpringBootApplication
@Slf4j
public class GetStudentDataByPosition {

    private static final int NAME_LENGTH = 20;
    private static final int RECORD_SIZE = 4 + NAME_LENGTH * 2 + 4; // 4 bytes por id + 20 caracteres * 2 bytes/caracteres + 4 bytes por integer (nota) = 48

    public GetStudentDataByPosition() {

    }

    public int getStudentInfoByPosition(File file) {

        Scanner scanner = new Scanner(System.in);

        if (file == null || !file.exists() || file.length() == 0) {
            log.warn("El archivo no existe o está vacío.");
            return -1;
        }

        long totalRecords = file.length() / RECORD_SIZE;

        log.info("Total de registros en el archivo: {} ", totalRecords);
        log.info("Recuerde: La posición a introducir debe estar entre 0 y {}", (totalRecords - 1));

        String input= scanner.next();
        int pos=-1;

        do{
            try {
                //Probamos a parsear la posición a ver si es entero
                pos = Integer.parseInt(input);
                break;
            }
            catch (NumberFormatException e) {
                log.warn("Entrada inválida. Por favor, ingrese un número entero.");
            }
        } while (true);


        if (pos < 0 || pos >= totalRecords) {
            log.warn("Posición inválida. Debe estar entre 0 y {}", (totalRecords - 1));
            return -1;
        }

        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            log.warn("------------------------------------------------------------------------------");
            log.info("LECTURA DE ALUMNOS");

            // Nos movemos al inicio del registro deseado
            raf.seek(pos * RECORD_SIZE);

            // Leemos el registro
            int id = raf.readInt();

            char[] nameChars = new char[NAME_LENGTH];
            for (int i = 0; i < NAME_LENGTH; i++) {
                nameChars[i] = raf.readChar();
            }
            String name = new String(nameChars).trim();

            float grade = raf.readFloat();

            log.info("Alumno leído [{}]: ID: {}, Nombre: {}, Nota: {}", pos, id, name, grade);

            log.warn("------------------------------------------------------------------------------");


        } catch (IOException e) {
            log.error("Error durante la lectura del archivo: {}", e.getMessage());
        }

        return pos;
    }



}
