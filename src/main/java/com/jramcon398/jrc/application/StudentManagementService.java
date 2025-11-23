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
     * Valida que un estudiante tenga todos los campos obligatorios correctos
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
     * Crea un nuevo módulo en el sistema.
     * Antes de insertarlo, comprueba si ya existe un módulo con el mismo código.
     * Si existe, lo devuelve; si no, lo inserta y devuelve la nueva entidad persistida.
     */
    @Override
    public Module createModule(Module module) {
        log.info("Creating module: {}", module);

        if (module == null) {
            throw new IllegalArgumentException("Module cannot be null");
        }

        //Some validations
        if (module.getCode() == null || module.getCode().isEmpty()) {
            throw new IllegalArgumentException("Module code cannot be null or empty");
        }
        if (module.getName() == null || module.getName().isEmpty()) {
            throw new IllegalArgumentException("Module name cannot be null or empty");
        }
        if (module.getHours() == null || module.getHours() <= 0) {
            throw new IllegalArgumentException("Module hours must be greater than 0");
        }

        //Check if exists
        Module existingModule = moduleRepository.findById(module.getId());
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
        Student existingStudent = studentRepository.findById(student.getId());
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
            var student = studentRepository.findById(studentId);
            if (student == null) throw new IllegalArgumentException("Student not found: " +
                    studentId);
            var module = moduleRepository.findById(moduleId);
            if (module == null) throw new IllegalArgumentException("Module not found: " + moduleId);
            Enrollment created = enrollmentRepository.insert(new Enrollment(LocalDate.now(), student.getId(), module.getId()));
            postgresqlDriver.commit();
            return created;
        } catch (Exception e) {
            postgresqlDriver.rollback();
            throw new RuntimeException("Error enrolling student in module", e);
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