package com.jramcon398.jrc;

import com.jramcon398.jrc.application.StudentService;
import com.jramcon398.jrc.model.Student;
import com.jramcon398.jrc.util.Constant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class JrcApplication implements CommandLineRunner {

    private final StudentService studentService;

    public static void main(String[] args) {
        SpringApplication.run(JrcApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        Student vito = new Student(15, "John", "asd@gmail.com", "Computer Science");
        boolean delete = studentService.deleteById(vito);

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


    }
}
