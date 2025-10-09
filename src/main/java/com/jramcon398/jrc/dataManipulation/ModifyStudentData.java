package com.jramcon398.jrc.dataManipulation;

import com.jramcon398.jrc.utils.InputValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;


/// Class ModifyStudentData: Class to modify a student's grade in a specific position
///                          The classes contained in dataManipulation are responsible for operations
///                          insertion, consultation and modification of student records.
///                          (They use the DataRW class for read and write operations
///                          in the binary file.)
///                          Use the GetStudentDataByPosition class to get the position
///                          of the student to be modified.

@SpringBootApplication
@Slf4j

public class ModifyStudentData {

    private static final int NAME_LENGTH = 20;
    private static final int RECORD_SIZE = 4 + NAME_LENGTH * 2 + 4; // 4 bytes id + 20 characters * 2 bytes/character + 4 bytes float (nota) = 48

    public ModifyStudentData() {

    }

    public void modifyStudentGrade(File file) {
        GetStudentDataByPosition studentDataByPosition = new GetStudentDataByPosition();
        int pos = studentDataByPosition.getStudentInfoByPosition(file);

        if (pos == -1) {
            log.warn("Could not modify grade because an error on input position.");
            return;
        }

        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            // Getting to the position of the student. Each record takes 48 bytes.
            raf.seek(pos * RECORD_SIZE);

            // Mantain id and name, only modify grade
            int id = raf.readInt();
            char[] nameChars = new char[NAME_LENGTH];
            for (int i = 0; i < NAME_LENGTH; i++) {
                nameChars[i] = raf.readChar();
            }

            String name = new String(nameChars).trim();
            Scanner scanner = new Scanner(System.in);
            String input;
            float grade = -1;

            //Grade validation
            do {
                log.info("Enter a new grade (0-10): ");
                input = scanner.next();
                if (InputValidator.isFloat(input)) {
                    grade = Float.parseFloat(input);
                    if (InputValidator.isValidGrade(grade)) {
                        break;
                    } else {
                        log.warn("Invalid grade. Must be between 0 y 10.");
                    }
                } else {
                    log.warn("Invalid input. Please, enter a valid number.");
                }
            } while (true);

            // Now we write the new grade
            raf.writeFloat(grade);
            log.warn("------------------------------------------------------------------------------");
            log.info("Student read: [{}]: ID: {}, Nombre: {}, Nota: {}", pos, id, name, grade);
            log.info("Student's grade at position {} properly modified {}.", pos, grade);
            log.warn("------------------------------------------------------------------------------");

        } catch (IOException e) {
            log.error("Error modifying file: {} ", e.getMessage());
        }




    }

}
