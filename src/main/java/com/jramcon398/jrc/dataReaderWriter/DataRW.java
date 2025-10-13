package com.jramcon398.jrc.dataReaderWriter;

import com.jramcon398.jrc.models.Student;
import com.jramcon398.jrc.utils.FileConstants;
import com.jramcon398.jrc.utils.FileUtils;
import com.jramcon398.jrc.utils.InputValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.jramcon398.jrc.utils.FileConstants;

import java.io.*;

/// Class DataRW: this class is used to execute operations on a binary file
///               that stores student records.
///               Operation management is found in RegistryManager class.

@SpringBootApplication
@Slf4j
public class DataRW {


    public DataRW() {
        //Constructor
    }

    public void readFile(File file) {

        //Checking if the file exists and is not empty
        if (checkFile(file)) {

            try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
                // Getting the number of records in the file
                long totalRecords = getRecordCount(file);
                log.warn("------------------------------------------------------------------------------");
                log.info("STUDENTS LIST (Registry Records: {})", totalRecords);

                for (int pos = 0; pos < totalRecords; pos++) {
                    // We go to the position of the record. Each record is 48 bytes long
                    raf.seek(pos * FileConstants.RECORD_SIZE);

                    int id = raf.readInt();
                    char[] nameChars = new char[FileConstants.NAME_LENGTH];

                    //Read the name character by character
                    for (int i = 0; i < FileConstants.NAME_LENGTH; i++) {
                        nameChars[i] = raf.readChar();
                    }
                    String name = new String(nameChars).trim();

                    float grade = raf.readFloat();

                    log.info("Student read: [{}]: ID: {}, Name: {}, Grade: {}", pos, id, name, grade);
                }

                log.warn("------------------------------------------------------------------------------");

            } catch (IOException e) {
                log.error("Error reading the file: {}", e.getMessage());
            }
        } else {
            log.warn("File doesn't exist or is empty.");
        }
    }

    public void writeFile(File file, Student student) {
        // Validations
        if (!InputValidator.isNonEmptyString(student.getName())) {
            log.error("Name cannot be empty.");
            return;
        }
        if (student.getGrade() < 0 || student.getGrade() > 10) {
            log.error("Grade must be between 0 and 10.");
            return;
        }

        // Write the student record to the file
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            raf.seek(raf.length());
            raf.writeInt(student.getId());
            String name = String.format("%-" + FileConstants.NAME_LENGTH + "s", student.getName());
            // Write the name character by character
            for (char c : name.toCharArray()) {
                raf.writeChar(c);
            }
            raf.writeFloat(student.getGrade());
            log.info("Added Student: ID={}, Name={}, Grade={}", student.getId(), student.getName(), student.getGrade());
        } catch (IOException e) {
            log.error("Error writing file: {}", e.getMessage());
        }
    }

    private boolean checkFile(File file) {

        return file.exists() && file.isFile() && (file.length() > 0);
    }

    public boolean idExists(File file, int id) {
        final int RECORD_SIZE = 4 + FileConstants.NAME_LENGTH * 2 + 4;
        // Checking file existence and non-emptiness
        if (!file.exists() || file.length() == 0) {
            return false;
        }
        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            //Record count
            long totalRecords = raf.length() / RECORD_SIZE;
            // We read only the IDs to check if the ID exists
            for (int pos = 0; pos < totalRecords; pos++) {
                raf.seek(pos * RECORD_SIZE); //we go to the position of the record
                int existingId = raf.readInt();
                if (existingId == id){
                    return true;
                }
            }
        } catch (IOException e) {
            log.error("Error writing file: {}", e.getMessage());
        }
        return false;
    }

    // Method to get the number of records in the file
    public long getRecordCount(File file) {
        final int RECORD_SIZE = 4 + FileConstants.NAME_LENGTH * 2 + 4;
        if (!file.exists() || file.length() == 0){
            return 0;
        }
        return file.length() / RECORD_SIZE;
    }


}
