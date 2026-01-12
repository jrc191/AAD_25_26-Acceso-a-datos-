package com.jramcon398.application;

import com.jramcon398.exceptions.ResourceNotFoundException;
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

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final ModuleRepository moduleRepository;

    // Enrollment with transaction management
    @Transactional
    public Enrollment enrollStudent(Long studentId, Long moduleId) {
        // Recover entity or throw exception if not found
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Module not found"));

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setModule(module);
        enrollment.setEnrollmentDate(LocalDate.now());

        return enrollmentRepository.save(enrollment);
    }

    // Enrollment with simulated error to test Rollback
    @Transactional
    public void enrollStudentWithError(Long studentId, Long moduleId) {
        this.enrollStudent(studentId, moduleId);
        throw new RuntimeException("Simulated error to test Rollback");
    }

    // Get enrollments with final grades above a threshold
    public List<Enrollment> getEnrollmentsWithHighGrades(Double minGrade) {
        return enrollmentRepository.findByMinFinalGrade(minGrade);
    }
}