package com.jramcon398.jrc.application;

import com.jramcon398.jrc.model.Student;
import com.jramcon398.jrc.repository.ModuleRepository;
import com.jramcon398.jrc.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentService implements CustomService<Student> {

    private final StudentRepository studentRepository;
    private final ModuleRepository moduleRepository;

//    public StudentService(StudentRepository studentRepository) {
//        this.studentRepository = studentRepository;
//    }

    /**
     * @param entity
     * @return
     */
    @Override
    public boolean validate(Student entity) {
        return !entity.getName().isBlank() && entity.getDni() > 0;
    }

    public Student createStudent(final Student student) {
        if (validate(student)) {
            return studentRepository.create(student);
        }
        return null;
    }

    public Student read(final Student student) {
        return studentRepository.read(student);
    }

    public boolean deleteById(final Student student) {
        if (validate(student)) {
            return studentRepository.delete(student);
        }
        return false;
    }

    public Student updateStudent(final Student student) {
        if (validate(student)) {
            return studentRepository.update(student);
        }
        return null;
    }

    public boolean deleteAll() {

        boolean result = studentRepository.deleteAll();

        return result;

    }
}
