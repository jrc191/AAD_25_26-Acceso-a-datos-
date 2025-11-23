package com.jramcon398.jrc.application;

import com.jramcon398.jrc.config.PostgresqlDriver;
import com.jramcon398.jrc.models.Enrollment;
import com.jramcon398.jrc.models.Module;
import com.jramcon398.jrc.models.Student;
import com.jramcon398.jrc.repository.EnrollmentRepository;
import com.jramcon398.jrc.repository.ModuleRepository;
import com.jramcon398.jrc.repository.StudentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.time.LocalDate;

@Service
@AllArgsConstructor
@Slf4j
public class StudentManagementService implements CustomService<Student> {

    private final PostgresqlDriver postgresqlDriver;
    private final StudentRepository studentRepository;
    private final ModuleRepository moduleRepository;
    private final EnrollmentRepository enrollmentRepository;

    /**
     * Some basic validation for Student entity.
     */
    @Override
    public boolean validateStudent(Student entity) {
        if (entity == null) {
            log.error("Cannot validate null student");
            return false;
        }

        boolean isValid = (entity.getName() != null && !entity.getName().isEmpty())
                && (entity.getNif() != null && !entity.getNif().isEmpty())
                && (entity.getEmail() != null && !entity.getEmail().isEmpty());

        if (!isValid) {
            log.warn("Student validation failed: {}", entity);
        }

        return isValid;
    }

    /**
     * Creates a new module in the system.
     * Before inserting, checks if a module with the same code already exists.
     * If exists, returns it; if not, inserts and returns the new persisted entity.
     */
    @Override
    public Module createModule(Module module) {
        log.info("Creating module: {}", module);

        if (module == null) {
            throw new IllegalArgumentException("Module cannot be null");
        }

        // Some validations
        if (module.getCode() == null || module.getCode().isEmpty()) {
            throw new IllegalArgumentException("Module code cannot be null or empty");
        }
        if (module.getName() == null || module.getName().isEmpty()) {
            throw new IllegalArgumentException("Module name cannot be null or empty");
        }
        if (module.getHours() == null || module.getHours() <= 0) {
            throw new IllegalArgumentException("Module hours must be greater than 0");
        }

        // Usar la versión sin conexión (que maneja su propia conexión)
        Module existingModule = moduleRepository.findByCode(module.getCode());
        if (existingModule != null) {
            log.info("Module with code {} already exists, returning existing module", module.getCode());
            return existingModule;
        }

        // Insertar el nuevo módulo
        Module createdModule = moduleRepository.insert(module);
        log.info("Module created successfully: {}", createdModule);
        return createdModule;
    }

    /**
     * Creates a new student in the system.
     * Before inserting, checks if a student with the same ID already exists.
     * If exists, returns it; if not, inserts and returns the new persisted entity.
     *
     * @param student
     * @return
     */
    @Override
    public Student createStudent(Student student) {
        log.info("Creating student: {}", student);

        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }

        //Some validations
        if (!validateStudent(student)) {
            throw new IllegalArgumentException("Student validation failed. Check name, NIF and email fields.");
        }

        //Check if exists
        Student existingStudent = studentRepository.findByNif(student.getNif());
        if (existingStudent != null) {
            log.warn("Student with NIF {} already exists, returning existing student", student.getNif());
            return existingStudent;
        }

        //Then insert
        Student createdStudent = studentRepository.insert(student);
        log.info("Student created successfully: {}", createdStudent);
        return createdStudent;
    }

    /**
     * Enrrolls a student in a module.
     * First checks if both student and module exist.
     * If any of them does not exist, throws an exception.
     * If both exist, creates a new enrollment record linking them.
     *
     * @param studentId
     * @param moduleId
     * @return
     */
    @Override
    public Enrollment enrollStudentInModule(Integer studentId, Integer moduleId) {
        try {
            postgresqlDriver.beginTransaction();
            Connection conn = postgresqlDriver.getConnection();

            // Check if student exists
            var student = studentRepository.findById(studentId, conn);
            if (student == null) {
                log.error("Cannot find student with id: {}", studentId);
                return null;
            }

            // Check if module exists
            var module = moduleRepository.findById(moduleId, conn);
            if (module == null) {
                log.error("Module not found: {}", moduleId);
                return null;

            }

            //Then enroll
            Enrollment created = enrollmentRepository.insert(new Enrollment(LocalDate.now(), student.getId(), module.getId()), conn);
            postgresqlDriver.commit();
            return created;
        } catch (Exception e) {
            postgresqlDriver.rollback();
            log.error("Error enrolling student {} in module {}: {}", studentId, moduleId, e.getMessage());
            return null;
        }
    }


    /**
     * Gets the number of enrollments for a given student.
     *
     * @param studentId
     * @return
     */
    public int getEnrollmentCount(Integer studentId) {
        log.info("Getting enrollment count for student {}", studentId);

        if (studentId == null) {
            throw new IllegalArgumentException("Student ID cannot be null");
        }

        int count = enrollmentRepository.countEnrollments(studentId);
        log.info("Student {} has {} enrollments", studentId, count);

        return count;
    }
}