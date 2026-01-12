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

    // Lógica principal de matriculación (Sección 5.2.2 - Ejemplo con éxito) [cite: 533]
    @Transactional
    public Enrollment enrollStudent(Long studentId, Long moduleId) {
        // 1. Recuperar entidades o lanzar excepción [cite: 534, 538]
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Module not found"));

        // 2. Crear matrícula
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setModule(module);
        enrollment.setEnrollmentDate(LocalDate.now());

        // 3. Guardar
        return enrollmentRepository.save(enrollment);
    }

    // Método para la PRÁCTICA 5.5: Simular error y Rollback [cite: 577, 578]
    @Transactional
    public void enrollStudentWithError(Long studentId, Long moduleId) {
        // Reutilizamos la lógica de matriculación
        this.enrollStudent(studentId, moduleId);

        // Simulamos un error después de guardar [cite: 582]
        // Gracias a @Transactional, el 'save' anterior se deshará (Rollback) [cite: 528]
        throw new RuntimeException("Simulated error to test Rollback");
    }

    // Consulta personalizada (Sección 6.5) [cite: 663]
    public List<Enrollment> getEnrollmentsWithHighGrades(Double minGrade) {
        return enrollmentRepository.findByMinFinalGrade(minGrade);
    }
}