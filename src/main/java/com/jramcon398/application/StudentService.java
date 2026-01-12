package com.jramcon398.application;

import com.jramcon398.exceptions.ResourceNotFoundException;
import com.jramcon398.models.Student;
import com.jramcon398.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for managing students.
 * Includes methods for retrieving, registering, deleting,
 * and searching students by email.
 */

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    // ReadOnly.
    @Transactional(readOnly = true)
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Student getStudentById(Long id) {
        // Exception if not found
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
    }

    // Writing ops
    @Transactional
    public Student registerStudent(Student student) {
        return studentRepository.save(student);
    }

    @Transactional
    public void deleteStudent(Long id) {
        // Checking existence, so we can throw a custom exception
        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cannot delete. Student not found with id: " + id);
        }
        studentRepository.deleteById(id);
    }

    // Searching by email containing text
    public List<Student> searchStudentsByEmail(String emailText) {
        return studentRepository.searchByEmail(emailText);
    }
}