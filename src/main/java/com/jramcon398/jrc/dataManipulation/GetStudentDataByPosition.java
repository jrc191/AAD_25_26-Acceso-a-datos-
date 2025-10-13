package com.jramcon398.jrc.dataManipulation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;
import com.jramcon398.jrc.utils.FileConstants;


/// Class GetStudentDataByPosition: Class to check student's info in a certain position
///                                 Obtained classes in dataManipulation handle operations
///                                 of student's records.
///                                 (They use DataRW to read and write in the binary file)

@SpringBootApplication
@Slf4j
public class GetStudentDataByPosition {

    public GetStudentDataByPosition() {

    }

    public int getStudentInfoByPosition(File file) {

        Scanner scanner = new Scanner(System.in);

        if (file == null || !file.exists() || file.length() == 0) {
            log.warn("File does not exist or is empty.");
            return -1;
        }

        long totalRecords = file.length() / FileConstants.RECORD_SIZE;

        log.info("Total file records: {} ", totalRecords);
        log.info("Remember: Position must be between 0 and {}", (totalRecords - 1));

        String input= scanner.next();
        int pos=-1;

        do{
            try {
                //Parse the input to an integer
                pos = Integer.parseInt(input);
                break;
            }
            catch (NumberFormatException e) {
                log.warn("Invalid input. Please, enter a valid number.");
            }
        } while (true);


        if (pos < 0 || pos >= totalRecords) {
            log.warn("Invalid Position. Position must be between 0 and {}", (totalRecords - 1));
            return -1;
        }

        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            log.warn("------------------------------------------------------------------------------");
            log.info("STUDENT INFO AT POSITION: {}", pos);

            // Go to the position of the student. Each record takes 48 bytes.
            raf.seek(pos * FileConstants.RECORD_SIZE);

            // Reading record data
            int id = raf.readInt();

            char[] nameChars = new char[FileConstants.NAME_LENGTH];
            for (int i = 0; i < FileConstants.NAME_LENGTH; i++) {
                nameChars[i] = raf.readChar();
            }
            String name = new String(nameChars).trim();

            float grade = raf.readFloat();

            log.info("Student [{}]: ID: {}, Name: {}, Grade: {}", pos, id, name, grade);

            log.warn("------------------------------------------------------------------------------");


        } catch (IOException e) {
            log.error("Error reading file: {}", e.getMessage());
        }

        return pos;
    }



}
