package com.jramcon398;

import com.jramcon398.application.EnrollmentService;
import com.jramcon398.application.StudentService;
import com.jramcon398.models.Module;
import com.jramcon398.models.Student;
import com.jramcon398.repository.ModuleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Test runner to execute JPA tests on application startup.
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class TestRunner implements CommandLineRunner {

    private final StudentService studentService;
    private final EnrollmentService enrollmentService;
    private final ModuleRepository moduleRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("================ STARTING JPA TESTS ================");

        // Master data
        Student s1 = new Student();
        s1.setName("Carlos Ruiz");
        s1.setEmail("carlos.ruiz@example.com");
        s1 = studentService.registerStudent(s1); //Register student. Checks validation
        log.info("Created Student: {} {}", s1.getName(), " (ID: " + s1.getId() + ")");

        Module m1 = new Module();
        m1.setName("Web Development with Spring Boot");
        m1.setHours(120);
        m1 = moduleRepository.save(m1);
        log.info("Created module: {} {}", m1.getName(), " (ID: " + m1.getId() + ")");

        // Checking enrollment
        log.info("\n--- Correct enrollment ---");
        try {
            enrollmentService.enrollStudent(s1.getId(), m1.getId());
            log.info("Enrollment pass CHECK.");
        } catch (Exception e) {
            log.error("Unexpected error: {} ", e.getMessage());
        }

        // Rollback test
        log.info("\n--- Checking Intended Rollback ---");
        try {
            enrollmentService.enrollStudentWithError(s1.getId(), m1.getId());
        } catch (RuntimeException e) {
            log.info("Exception catch succeded " + e.getMessage());
            log.info("   (Check the logs to verify that no enrollment was created)");
        }

        // Validation test
        log.info("\n--- Checking validation tests ---");
        try {
            Student invalidStudent = new Student();
            invalidStudent.setName(""); // Empty name (invalid)
            invalidStudent.setEmail("noproperemail"); // No format email
            studentService.registerStudent(invalidStudent);
        } catch (Exception e) {
            log.info("Validations working properly. Error: {} ", e.getMessage());
        }

        log.info("\n================ TEST ENDED ================");
    }
}