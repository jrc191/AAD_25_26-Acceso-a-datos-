package com.jramcon398.jrc.application;

import com.jramcon398.jrc.models.Student;
import org.springframework.stereotype.Service;

@Service
public class StudentManagementService implements CustomService<Student> {

    /**
     * @param entity
     * @return
     */
    @Override
    public boolean validate(Student entity) {
        return !entity.getName().isEmpty() && entity.getId() > 0 && entity.getNif().isEmpty() && entity.getEmail().isEmpty();
    }

    /**
     * @param module
     * @return
     */
    @Override
    public Module createModule(Module module) {
        return null;
    }

    /**
     * @param student
     * @return
     */
    @Override
    public Student createStudent(Student student) {
        return null;
    }

    /**
     * @param studentId
     * @param moduleId
     * @return
     */
    @Override
    public Student enrollStudentInModule(Integer studentId, Integer moduleId) {
        return null;
    }


}
