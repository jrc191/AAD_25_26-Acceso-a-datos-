package com.jramcon398.jrc.application;

import com.jramcon398.jrc.models.Enrollment;
import com.jramcon398.jrc.models.Module;
import com.jramcon398.jrc.models.Student;

public interface CustomService<T> {

    boolean validateStudent(T entity);

    Module createModule(Module module);

    T createStudent(Student student);

    Enrollment enrollStudentInModule(Integer studentId, Integer moduleId);

}