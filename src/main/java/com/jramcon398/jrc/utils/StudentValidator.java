package com.jramcon398.jrc.utils;

import com.jramcon398.jrc.models.Module;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@UtilityClass
public class StudentValidator {

    /**
     * Validation methods for Student fields
     */

    public Integer validateId(Integer id) {
        if (id == null) {
            return null;
        }

        if (id <= 0) {
            log.error("Student ID must be positive. Received: {}", id);
            return Constants.DEFAULT_STUDENT_ID;
        }

        return id;
    }

    public String validateNif(String nif) {
        if (nif == null || nif.trim().isEmpty()) {
            log.error("Student NIF cannot be null or empty. Setting default: {}", Constants.DEFAULT_NIF);
            return Constants.DEFAULT_NIF;
        }
        return nif;
    }

    public String validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            log.error("Student name cannot be null or empty. Setting default: {}", Constants.DEFAULT_NAME);
            return Constants.DEFAULT_NAME;
        }
        return name;
    }

    public String validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            log.error("Student email cannot be null or empty. Setting default: {}", Constants.DEFAULT_EMAIL);
            return Constants.DEFAULT_EMAIL;
        }

        if (!email.contains("@")) {
            log.warn("Student email '{}' seems invalid (missing @). Using anyway.", email);
        }

        return email;
    }

    public String validateCourse(String course) {
        if (course == null || course.trim().isEmpty()) {
            log.error("Student course cannot be null or empty. Setting default: {}", Constants.DEFAULT_COURSE);
            return Constants.DEFAULT_COURSE;
        }
        return course;
    }

    public List<Module> validateModules(List<Module> modules) {
        if (modules == null) {
            log.error("Module list cannot be null. Returning empty list.");
            return new ArrayList<>();
        }
        return modules;
    }

}
