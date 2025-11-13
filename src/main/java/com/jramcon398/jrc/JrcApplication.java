package com.jramcon398.jrc;

import com.jramcon398.jrc.application.StudentManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class JrcApplication implements CommandLineRunner {
    private final StudentManagementService studentManagementService;

    public static void main(String[] args) {
        SpringApplication.run(JrcApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //Student miriam = new Student(null, "66280457T", "Miriam", "miriam@g.educaand.es", "DAW", List.of());
        //Module programacion = new Module(null, "0485", "Programación", 250);
        //miriam = studentManagementService.createStudent(miriam);
        //programacion = studentManagementService.createModule(programacion);
        //studentManagementService.enrollStudentInModule(miriam.getId(), programacion.getId());
        //studentRepository.delete(miriam.getId());
    }
}
