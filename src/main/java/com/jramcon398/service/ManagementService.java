package com.jramcon398.service;

import com.jramcon398.model.Enrollment;
import com.jramcon398.model.Module;
import com.jramcon398.model.Student;
import com.jramcon398.repository.EnrollmentRepository;
import com.jramcon398.repository.ModuleRepository;
import com.jramcon398.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ManagementService {

    private final StudentRepository studentRepository;
    private final ModuleRepository moduleRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Transactional
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    @Transactional
    public Module createModule(Module module) {
        return moduleRepository.save(module);
    }

    @Transactional
    public Enrollment enrollStudentInModule(Long studentId, Long moduleId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado"));
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Módulo no encontrado"));

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setModule(module);
        enrollment.setEnrollmentDate(LocalDate.now());

        return enrollmentRepository.save(enrollment);
    }

    @Transactional(readOnly = true)
    public int countEnrollments(Long studentId) {
        return (int) enrollmentRepository.countByStudentId(studentId);
    }
}