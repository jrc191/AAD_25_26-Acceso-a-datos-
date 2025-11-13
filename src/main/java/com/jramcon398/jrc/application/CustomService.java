package com.jramcon398.jrc.application;

import com.jramcon398.jrc.models.Student;

public interface CustomService<T> {

    boolean validate(T entity);

    Module createModule(Module module);

    T createStudent(Student student);

    T enrollStudentInModule(Integer studentId, Integer moduleId);

}