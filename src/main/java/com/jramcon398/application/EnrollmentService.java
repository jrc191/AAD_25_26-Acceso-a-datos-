package com.jramcon398.application;

import com.jramcon398.exceptions.ResourceNotFoundException;
import com.jramcon398.exceptions.SimulatedException;
import com.jramcon398.models.Enrollment;
import com.jramcon398.models.Module;
import com.jramcon398.models.Student;
import com.jramcon398.repository.EnrollmentRepository;
import com.jramcon398.repository.ModuleRepository;
import com.jramcon398.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Service class for managing enrollments.
 * Includes methods for enrolling students, handling errors,
 * and retrieving enrollments based on final grades.
 */

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final ModuleRepository moduleRepository;

    // Default enrollment method
    @Transactional
    public Enrollment enrollStudent(Long studentId, Long moduleId) {
        // Search for Student and Module
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Module not found"));

        // Then we save the Enrollment. First we create it.
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setModule(module);
        enrollment.setEnrollmentDate(LocalDate.now());

        return enrollmentRepository.save(enrollment);
    }


    // Simulated error to test Rollback
    @Transactional
    public void enrollStudentWithError(Long studentId, Long moduleId) {

        // Recover Student and Module
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Module not found"));

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setModule(module);
        enrollment.setEnrollmentDate(LocalDate.now());

        enrollmentRepository.save(enrollment);

        // Forcing rollback
        throw new SimulatedException("Simulated error to test Rollback");

    }

    // Custom query: enrollments with final grade >= minGrade
    public List<Enrollment> getEnrollmentsWithHighGrades(Double minGrade) {
        return enrollmentRepository.findByMinFinalGrade(minGrade);
    }
}