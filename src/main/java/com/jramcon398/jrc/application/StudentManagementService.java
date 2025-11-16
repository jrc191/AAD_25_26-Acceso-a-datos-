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
     * @param entity
     * @return
     */
    @Override
    public boolean validateStudent(Student entity) {
        if (entity == null) {
            log.error("Cannot validate null student");
            return false;
        }
        return (entity.getId() != null && entity.getId() > 0)
                && (entity.getName() != null && !entity.getName().isEmpty())
                && (entity.getNif() != null && !entity.getNif().isEmpty())
                && (entity.getEmail() != null && !entity.getEmail().isEmpty())
                && (entity.getCourse() != null && !entity.getCourse().isEmpty());
    }

    public boolean validateModule(Module entity) {
        if (entity == null) {
            log.error("Cannot validate null module");
            return false;
        }
        return (entity.getId() != null && entity.getId() > 0)
                && (entity.getName() != null && !entity.getName().isEmpty())
                && (entity.getCode() != null && !entity.getCode().isEmpty())
                && (entity.getHours() != null && entity.getHours() > 0);
    }

    /**
     * @param entity
     * @return
     */
    @Override
    public Module createModule(Module entity) {
        if (entity == null) {
            log.error("Cannot create null module. Creating default module.");
            return new Module(0, "NO-CODE", "NO-NAME", 0);
        }

        try {

            Module existingModule = moduleRepository.findById(entity.getId());
            if (existingModule != null) {
                log.info("Module with code {} already exists. Returning existing module.", entity.getCode());
                return existingModule;
            }

            if (!validateModule(entity)) {
                log.error("Module validation failed for module: {}. Returning module with applied defaults.", entity);
                return entity;
            }

            Module savedModule = moduleRepository.insert(entity);
            if (savedModule == null) {
                log.error("Repository returned null when saving module. Returning original module.");
                return entity;
            }

            log.info("Module created successfully: {}", savedModule);
            return savedModule;


        } catch (Exception e) {
            log.error("Error creating module: {}. Returning module with defaults.", e.getMessage(), e);
            return entity;
        }
    }

    /**
     * @param entity
     * @return
     */
    @Override
    public Student createStudent(Student entity) {
        if (entity == null) {
            log.error("Cannot create null student. Creating default student.");
            return new Student(0, "UNKNOWN", "Unnamed Student", "no-email@unknown.com", "UNKNOWN");
        }

        try {

            Student existingStudent = studentRepository.findById(entity.getId());
            if (existingStudent != null) {
                log.info("Student with ID {} already exists. Returning existing student.", entity.getNif());
                return existingStudent;
            }

            if (!validateStudent(entity)) {
                log.error("Student validation failed for student: {}. Returning student with defaults.", entity);
                return entity;
            }

            Student savedStudent = studentRepository.insert(entity);
            if (savedStudent == null) {
                log.error("Repository returned null when saving student. Returning original student.");
                return entity;
            }

            log.info("Student created successfully: {}", savedStudent);
            return savedStudent;

        } catch (Exception e) {
            log.error("Error creating student: {}. Returning student with defaults.", e.getMessage(), e);
            return entity;
        }
    }

    /**
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
            if (module == null) throw new IllegalArgumentException("Module not found: " +
                    moduleId);
            Enrollment created = enrollmentRepository.insert(new Enrollment(LocalDate.now(), null, student.getId(), module.getId()));
            postgresqlDriver.commit();
            return created;
        } catch (Exception e) {
            postgresqlDriver.rollback();
            throw new RuntimeException("Error enrolling student in module", e);
        }
    }

    private Student createDefaultStudent() {
        Student defaultStudent = new Student(0, "UNKNOWN", "Unnamed Student", "no-email@unknown.com", "UNKNOWN");
        log.debug("Created default student: {}", defaultStudent);
        return defaultStudent;
    }


}
