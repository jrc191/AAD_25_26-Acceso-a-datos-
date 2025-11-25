package com.jramcon398.jrc.application;

import com.jramcon398.jrc.models.Enrollment;
import com.jramcon398.jrc.models.Module;
import com.jramcon398.jrc.models.Student;
import com.jramcon398.jrc.repository.EnrollmentRepository;
import com.jramcon398.jrc.repository.ModuleRepository;
import com.jramcon398.jrc.repository.StudentRepository;
import com.jramcon398.jrc.utils.EnrollmentValidator;
import com.jramcon398.jrc.utils.ModuleValidator;
import com.jramcon398.jrc.utils.StudentValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class StudentManagementService implements CustomService<Student> {

    private final StudentRepository studentRepository;
    private final ModuleRepository moduleRepository;
    private final EnrollmentRepository enrollmentRepository;

    /**
     * Validates Student entity using StudentValidator utility class.
     * Returns true if all fields are valid, false otherwise.
     *
     * @param entity Student to validate
     * @return boolean indicating if student is valid
     */
    @Override
    public boolean validateStudent(Student entity) {
        if (entity == null) {
            log.error("Cannot validate null student");
            return false;
        }

        try {
            // Validations
            StudentValidator.validateId(entity.getId());
            StudentValidator.validateNif(entity.getNif());
            StudentValidator.validateName(entity.getName());
            StudentValidator.validateEmail(entity.getEmail());
            StudentValidator.validateCourse(entity.getCourse());
            StudentValidator.validateModules(entity.getModules());

            log.info("Student validation passed: {}", entity);
            return true;
        } catch (Exception e) {
            log.warn("Student validation failed: {} - {}", entity, e.getMessage());
            return false;
        }
    }

    /**
     * Creates a new module in the system.
     * Before inserting, checks if a module with the same code already exists.
     * If exists, returns it; if not, inserts and returns the new persisted entity.
     *
     * @param module Module to create
     * @return Created Module or existing one if code already exists
     */
    @Override
    public Module createModule(Module module) {
        log.info("Creating module: {}", module);

        if (module == null) {
            throw new IllegalArgumentException("Module cannot be null");
        }

        // Validate module using ModuleValidator
        Module validatedModule = new Module();
        validatedModule.setId(ModuleValidator.validateId(module.getId()));
        validatedModule.setCode(ModuleValidator.validateCode(module.getCode()));
        validatedModule.setName(ModuleValidator.validateName(module.getName()));
        validatedModule.setHours(ModuleValidator.validateHours(module.getHours()));

        // Check if module with same code already exists. If so, return it
        Module existingModule = moduleRepository.findByCode(validatedModule.getCode());
        if (existingModule != null) {
            log.warn("Module with code {} already exists, returning existing module", validatedModule.getCode());
            return existingModule;
        }

        // if not, insert new module
        Module createdModule = moduleRepository.insert(validatedModule);
        log.info("Module created successfully: {}", createdModule);
        return createdModule;
    }

    /**
     * Creates a new student in the system.
     * Before inserting, checks if a student with the same ID already exists.
     * If exists, returns it; if not, inserts and returns the new persisted entity.
     *
     * @param student Student to create
     * @return Created Student or existing one if ID already exists
     */
    @Override
    public Student createStudent(Student student) {
        log.info("Creating student: {}", student);

        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }

        // Validations
        Student validatedStudent = new Student();
        validatedStudent.setId(StudentValidator.validateId(student.getId()));
        validatedStudent.setNif(StudentValidator.validateNif(student.getNif()));
        validatedStudent.setName(StudentValidator.validateName(student.getName()));
        validatedStudent.setEmail(StudentValidator.validateEmail(student.getEmail()));
        validatedStudent.setCourse(StudentValidator.validateCourse(student.getCourse()));
        validatedStudent.setModules(StudentValidator.validateModules(student.getModules()));

        // Check if existing student
        Student existingStudent = studentRepository.findByNif(validatedStudent.getNif());
        if (existingStudent != null) {
            log.warn("Student with NIF {} already exists, returning existing student", validatedStudent.getNif());
            return existingStudent;
        }

        // Then insert
        Student createdStudent = studentRepository.insert(validatedStudent);
        log.info("Student created successfully: {}", createdStudent);
        return createdStudent;
    }

    /**
     * Enrolls a student in a module.
     * First checks if both student and module exist.
     * If any of them does not exist, throws an exception.
     * If both exist, creates a new enrollment record linking them.
     *
     * @param studentId ID of the student to enroll
     * @param moduleId  ID of the module to enroll the student in
     * @return Created Enrollment record linking student and module
     */
    @Override
    @Transactional
    public Enrollment enrollStudentInModule(Integer studentId, Integer moduleId) {

        // Validations
        Integer validatedStudentId = EnrollmentValidator.validateStudentId(studentId);
        Integer validatedModuleId = EnrollmentValidator.validateModuleId(moduleId);

        // Check existence of student, so we can enroll
        var student = studentRepository.findById(validatedStudentId);
        if (student == null) {
            log.error("Cannot find student with id: {}", validatedStudentId);
            throw new RuntimeException("Student not found with id: " + validatedStudentId);
        }

        // Check if module exists, so we can enroll
        var module = moduleRepository.findById(validatedModuleId);
        if (module == null) {
            log.error("Module not found: {}", validatedModuleId);
            throw new RuntimeException("Module not found with id: " + validatedModuleId);
        }

        // Create enrollment with validated date
        LocalDate enrollmentDate = EnrollmentValidator.validateDate(LocalDate.now());
        Enrollment enrollment = new Enrollment(enrollmentDate, student.getId(), module.getId());

        Enrollment created = enrollmentRepository.insert(enrollment);
        log.info("Successfully enrolled student {} in module {}", validatedStudentId, validatedModuleId);
        return created;
    }

    /**
     * Gets the number of enrollments for a given student.
     *
     * @param studentId ID of the student to get enrollment count for
     */
    public void getEnrollmentCount(Integer studentId) {
        log.info("Getting enrollment count for student {}", studentId);

        if (studentId == null) {
            throw new IllegalArgumentException("Student ID cannot be null");
        }

        int count = enrollmentRepository.countEnrollments(studentId);
        log.info("Student {} has {} enrollments", studentId, count);

    }
}