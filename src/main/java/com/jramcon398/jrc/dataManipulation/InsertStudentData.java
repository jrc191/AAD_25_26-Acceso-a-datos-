package com.jramcon398.jrc.dataManipulation;

import com.jramcon398.jrc.dataReaderWriter.DataRW;
import com.jramcon398.jrc.models.Student;
import com.jramcon398.jrc.utils.InputValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.Scanner;


/// Class InsertStudentData: Class to insert new student data (sequentially).
///                          Classes in dataManipulation handle operations
///                          of insert data, checking student's info and registry modification.
///                          (They use DataRW to read and write student in binary file.)

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

        // Validations and input reading
        do {
            log.info("Enter student's id: FORMAT numeric>0 (enter EXIT to exit): ");
            String input = scanner.next();

            if (input.equals("EXIT")) {
                break;
            }

            if (InputValidator.isInteger(input)) {
                id = Integer.parseInt(input);
                if (id > 0) {
                    if (dataRW.idExists(file, id)) {
                        log.warn("Id already exists. Please enter something differently.");
                    } else {
                        fullContent += id + ";";
                        break;
                    }
                } else {
                    log.warn("Id must be greater than 0.");
                }
            } else {
                log.warn("Invalid file. Must be a number or 'EXIT'. Please, try it again.");
            }
        } while (true);

        do {
            log.info("Enter Student's name: (20 characters max.) (Enter EXIT to exit): ");
            String input = scanner.next();

            if (input.equals("EXIT")) {
                break;
            }

            if (InputValidator.isNonEmptyString(input) && input.length() <= 20) {
                name = input;
                fullContent += name + ";";
                break;
            } else {
                log.warn("Name must be max. 20 characters and cannot be empty.");
            }
        } while (true);

        do {
            log.info("Student's grade: FORMAT 0-10 (Enter EXIT to exit): ");
            String input = scanner.next();

            if (input.equals("EXIT")) {
                break;
            }

            if (InputValidator.isFloat(input)) {
                grade = Float.parseFloat(input);
                if (InputValidator.isValidGrade(grade)) {
                    fullContent += grade + ";";
                    break;
                } else {
                    log.warn("Grade must be between 0 and 10.");
                }
            } else {
                log.warn("Invalid input. Should be a number or 'EXIT'. Try it again.");
            }
        } while (true);


        log.info("\nText:\n{}", fullContent);

        // Writing the student to the file
        Student student = new Student(id, name, grade);
        log.info("Student created: ID={}, Name={}, Grade={}", student.getId(), student.getName(), student.getGrade());
        dataRW.writeFileObject(file, student);

    }

}
