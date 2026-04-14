package com.jramcon398;

import com.jramcon398.model.Enrollment;
import com.jramcon398.model.Module;
import com.jramcon398.model.Profile;
import com.jramcon398.model.Student;
import com.jramcon398.repository.StudentRepository;
import com.jramcon398.service.ManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.jramcon398.repository")
@EntityScan(basePackages = "com.jramcon398.model")
@RequiredArgsConstructor
public class rcApplication implements CommandLineRunner {

    private final ManagementService managementService;
    private final StudentRepository studentRepository;

    public static void main(String[] args) {
        SpringApplication.run(JrcApplication.class, args);
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("--- INICIO DE LA PRUEBA ---");

        Profile perfil = new Profile();
        perfil.setAddress("Calle Falsa 123");
        perfil.setPhone("600112233");

        Student miriam = new Student();
        miriam.setNif("66280457T");
        miriam.setName("Miriam");
        miriam.setEmail("miriam@g.educaand.es");
        miriam.setCourse("DAW");
        miriam.setProfile(perfil);

        Module programacion = new Module();
        programacion.setCode("0485");
        programacion.setName("Programación");
        programacion.setHours(250);

        // Uses managementService per assignment requirements
        miriam = managementService.createStudent(miriam);
        log.info("Alumno creado: {}", miriam);

        programacion = managementService.createModule(programacion);
        log.info("Módulo creado: {}", programacion);

        Enrollment enrollment = managementService.enrollStudentInModule(miriam.getId(), programacion.getId());
        log.info("Matricula realizada: {}", enrollment);

        int countEnrollments = managementService.countEnrollments(miriam.getId());
        log.info("{} módulos matriculados para el alumno {}", countEnrollments, miriam.getName());

        miriam = studentRepository.findByNif(miriam.getNif())
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado"));
        log.info("Alumno recuperado: {}", miriam);

        //studentRepository.delete(miriam);
        //log.info("Alumno {} eliminado", miriam.getName());
        //throw new RuntimeException("Forzando rollback de la transacción");
    }
}