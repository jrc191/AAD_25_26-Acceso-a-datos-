package com.jramcon398.jrc;

import com.jramcon398.jrc.application.StudentService;
import com.jramcon398.jrc.model.Student;
import com.jramcon398.jrc.util.Constant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class JrcApplication implements CommandLineRunner {

    private final StudentService studentService;
    Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        SpringApplication.run(JrcApplication.class, args);
    }

/*    @Override
    public void run(String... args) throws Exception {

        Student vito = new Student(15, "John", "asd@gmail.com", "Computer Science");
        boolean delete = studentService.deleteById(vito);

        try {
            if (delete) {
                log.info("Delete: {}", delete);
            } else {
                log.error(Constant.STUDENT_NOT_FOUND);
            }


            Student create = studentService.createStudent(vito);
            if (create != null) {
                log.info("Create: {}", create);
            } else {
                log.error(Constant.STUDENT_NOT_FOUND);
            }



            Student stud = studentService.read(vito);
            if (stud != null) {
                log.info("Read: {}", stud);
            } else {
                log.error(Constant.STUDENT_NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
        }


    }
    */

    @Override
    public void run(String... args) throws Exception {


        try {
            int option;
            do {
                option = menuTest();
            } while (option != 5);
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
        }

    }

    public int menuTest() {
        int option = 0;
        log.info("Seleccione una opción:");

        log.info("Menu");
        log.info("1. Registrar DUPLICADO");
        log.info("2. Listar");
        log.info("3. Actualizar SIN EXISTIR");
        log.info("4. Eliminar SIN WHERE");
        log.info("5. Salir");

        option = scanner.nextInt();
        switch (option) {
            case 1:
                log.info("Registrar estudiante DUPLICADO");
                duplicatedInsertion();

                break;
            case 2:
                log.info("Listar estudiantes");
                break;
            case 3:
                log.info("Actualizar estudiante NO EXISTENTE");
                updateNonExistentStudent();
                break;
            case 4:
                log.info("Eliminar estudiante SIN WHERE");
                deleteWithoutWhere();
                break;
            case 5:
                log.info("Salir");
                break;
            default:
                log.info("Opción inválida");
                break;
        }

        return option;

    }

    private void duplicatedInsertion() {
        Student student = new Student(15, "John", "asd@gmail.com", "Computer Science");
        Student create = studentService.createStudent(student);

        if (create != null) {
            log.info("Create: {}", create);
        } else {
            log.error(Constant.STUDENT_NOT_FOUND);
        }

    }

    private void updateNonExistentStudent() {
        Student student = new Student(2, "Non Existent", "none", "None");
        Student updatedStudent = studentService.updateStudent(student);

        if (updatedStudent != null) {
            log.info("Create: {}", updatedStudent);
        } else {
            log.error(Constant.STUDENT_NOT_FOUND);
        }
    }

    private void deleteWithoutWhere() {
        boolean delete = studentService.deleteAll();

        if (delete) {
            log.info("Delete: {}", delete);
        } else {
            log.error(Constant.STUDENT_NOT_FOUND);
        }
    }


}
