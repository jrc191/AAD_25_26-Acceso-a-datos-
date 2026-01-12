package com.jramcon398.application; // O package com.jramcon398.service;

import com.jramcon398.exceptions.ResourceNotFoundException;
import com.jramcon398.models.Student;
import com.jramcon398.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service // Marca la clase como un Bean de servicio de Spring [cite: 441]
@RequiredArgsConstructor // Inyección de dependencias por constructor (Lombok) [cite: 440]
public class StudentService {

    private final StudentRepository studentRepository; // Dependencia final [cite: 468]

    // Lectura: Puede ser transaccional solo lectura para optimizar [cite: 604]
    @Transactional(readOnly = true)
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Student getStudentById(Long id) {
        // Uso de orElseThrow como indica la Sección 7.3 [cite: 737, 738]
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
    }

    // Escritura: Transaccional por defecto [cite: 559, 560]
    @Transactional
    public Student registerStudent(Student student) {
        return studentRepository.save(student);
    }

    @Transactional
    public void deleteStudent(Long id) {
        // Verificamos si existe antes de borrar para lanzar excepción propia si no
        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cannot delete. Student not found with id: " + id);
        }
        studentRepository.deleteById(id);
    }

    // Método para la búsqueda personalizada definida en el repositorio (Sección 6.6)
    public List<Student> searchStudentsByEmail(String emailText) {
        return studentRepository.searchByEmail(emailText);
    }
}