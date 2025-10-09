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

/// RegistryManager Class: This class is used to execute operations
///                        insertion, consultation or modification of students through
///                        a menu.
///                        Implements dataManipulation classes to perform the
///                        operations.


@SpringBootApplication
@Slf4j
public class RegistryManager {

    public RegistryManager() {

    }

    public void menuText(){
        log.info("---------- OPTIONS MENU ----------");
        log.info("1. Read student's registry file");
        log.info("2. Insert new student");
        log.info("3. Modify student's grade");
        log.info("4. Get student by position");
        log.info("5. Exit");
        log.info("--------------------------------------");
    }

    public void menu(File file){

        Scanner scanner = new Scanner(System.in);
        int op=-1;
        boolean cancel=false;

        do {
            menuText();
            log.info("Enter the desired option (enter 5 to exit the program): ");
            String input = scanner.next();

            try {
                //Parsing the input to integer
                op = Integer.parseInt(input);

                switch (op) {
                    case 1:
                        log.info("Option 1: Read student's registry file");
                        GetAllStudentsData studentsData = new GetAllStudentsData();
                        studentsData.getAllStudentData(file);
                        break;
                    case 2:
                        log.info("Option 2: Insert new student");
                        InsertStudentData insertStudentData = new InsertStudentData();
                        insertStudentData.insertStudentData(file);
                        break;
                    case 3:
                        log.info("Option 3: Modify student's grade");
                        studentsData = new GetAllStudentsData();
                        studentsData.getAllStudentData(file);
                        ModifyStudentData modifyStudentData = new ModifyStudentData();
                        modifyStudentData.modifyStudentGrade(file);
                        break;
                    case 4:
                        log.info("Option 4: Get student by position");
                        GetStudentDataByPosition studentDataByPosition = new GetStudentDataByPosition();
                        studentDataByPosition.getStudentInfoByPosition(file);

                        break;
                    case 5:
                        log.info("Exiting...");
                        cancel=true;
                        break;
                    default:
                        log.warn("Invalid option. Please, input a valid option between 1 and 5.");
                        break;
                }

            } catch (NumberFormatException e) {
                log.warn("Invalid input. Should be a integer number or 'EXIT'. Try it again.");
            }

        } while (!cancel || op<1 || op>5);

    }


}
