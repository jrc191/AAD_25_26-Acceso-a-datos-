package com.jramcon398.jrc.dataManipulation;

import com.jramcon398.jrc.dataReaderWriter.DataRW;
import com.jramcon398.jrc.models.Student;
import com.jramcon398.jrc.utils.FileConstants;
import com.jramcon398.jrc.utils.InputValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.Scanner;


/// Class InsertStudentData: Class to insert new student data (sequentially).
///                          Classes in dataManipulation handle operations
///                          of insert data, checking student's info and registry modification.
///                          (They use DataRW to read and write student in binary file.)

@Slf4j
public class InsertStudentData {

    DataRW dataRW = new DataRW();

    public void insertStudentData(File file) {
        Scanner scanner = new Scanner(System.in);
        int id = -1;
        String name = "";
        float grade = -1;

        try {
            //Validations
            id = getValidId(scanner, file);
            if (id == -1) {
                log.warn("Operation cancelled or invalid ID input.");
                return;
            }

            name = getValidName(scanner);
            if (name.isEmpty()) {
                log.warn("Operation cancelled or invalid name input.");
                return;
            }

            grade = getValidGrade(scanner);
            if (grade == -1) {
                log.warn("Operation cancelled or invalid grade input.");
                return;
            }

            // Create and write the student
            Student student = new Student(id, name, grade);
            log.info("Student created: ID={}, Name={}, Grade={}", student.getId(), student.getName(), student.getGrade());
            dataRW.writeFile(file, student);

        } catch (Exception e) {
            log.error("Operation failed: {}", e.getMessage());
        }
    }

    private int getValidId(Scanner scanner, File file) {
        do {
            log.info("Enter student's id: FORMAT numeric>0 (enter EXIT to exit): ");
            String input = scanner.next();

            if (input.equals("EXIT")) {
                return -1;
            }

            if (InputValidator.isInteger(input)) {
                int id = Integer.parseInt(input);
                if (id > 0) {
                    if (dataRW.idExists(file, id)) {
                        log.warn("Id already exists. Please enter something differently.");
                    } else {
                        return id;
                    }
                } else {
                    log.warn("Id must be greater than 0.");
                }
            } else {
                log.warn("Invalid input. Must be a number or 'EXIT'. Please, try it again.");
            }
        } while (true);
    }

    private String getValidName(Scanner scanner) {
        do {
            log.info("Enter Student's name: ({} characters max.) (Enter EXIT to exit): ", FileConstants.NAME_LENGTH);
            String input = scanner.next();

            if (input.equals("EXIT")) {
                return "";
            }

            if (InputValidator.isNonEmptyString(input) && input.length() <= FileConstants.NAME_LENGTH) {
                return input;
            } else {
                log.warn("Name must be max. {} characters and cannot be empty.", FileConstants.NAME_LENGTH);
            }
        } while (true);
    }

    private float getValidGrade(Scanner scanner) {
        do {
            log.info("Student's grade: FORMAT 0-10 (Enter EXIT to exit): ");
            String input = scanner.next();

            if (input.equals("EXIT")) {
                return -1;
            }

            if (InputValidator.isFloat(input)) {
                float grade = Float.parseFloat(input);
                if (InputValidator.isValidGrade(grade)) {
                    return grade;
                } else {
                    log.warn("Grade must be between 0 and 10.");
                }
            } else {
                log.warn("Invalid input. Should be a number or 'EXIT'. Try it again.");
            }
        } while (true);
    }

}
