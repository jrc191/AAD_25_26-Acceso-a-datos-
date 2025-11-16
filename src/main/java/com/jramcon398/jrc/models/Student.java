package com.jramcon398.jrc.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Slf4j
@ToString
public class Student {

    private Integer id;
    private String nif;
    private String name;
    private String email;
    private String course;
    private List<Module> modules;

    // Constructor with all parameters
    public Student(Integer id, String nif, String name, String email, String course, List<Module> modules) {
        this.id = validateId(id);
        this.nif = validateNif(nif);
        this.name = validateName(name);
        this.email = validateEmail(email);
        this.course = validateCourse(course);
        this.modules = validateModules(modules);
    }

    // Constructor without modules (your existing one)
    public Student(Integer id, String nif, String name, String email, String course) {
        this.id = validateId(id);
        this.nif = validateNif(nif);
        this.name = validateName(name);
        this.email = validateEmail(email);
        this.course = validateCourse(course);
        this.modules = new ArrayList<>();
    }

    private Integer validateId(Integer id) {
        if (id == null) {
            log.error("Student ID cannot be null. Setting default value: 0");
            return 0;
        }
        return id;
    }

    private String validateNif(String nif) {
        if (nif == null || nif.trim().isEmpty()) {
            log.error("Student NIF cannot be null or empty. Setting default value: 'NO-NIF'");
            return "NO-NIF";
        }
        return nif;
    }

    private String validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            log.error("Student name cannot be null or empty. Setting default value: 'NO-NAME'");
            return "NO-NAME";
        }
        return name;
    }

    private String validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            log.error("Student email cannot be null or empty. Setting default value: 'no-email@unknown.com'");
            return "no-email@unknown.com";
        }
        // TODO: REGEX VALIDATION
        if (!email.contains("@")) {
            log.warn("Student email '{}' appears to be invalid (missing @). Using anyway.", email);
        }
        return email;
    }

    private String validateCourse(String course) {
        if (course == null || course.trim().isEmpty()) {
            log.error("Student course cannot be null or empty. Setting default value: 'NO-COURSE'");
            return "NO-COURSE";
        }
        return course;
    }

    private List<Module> validateModules(List<Module> modules) {
        if (modules == null) {
            log.error("Student modules list cannot be null. Setting default value: empty list");
            return new ArrayList<>();
        }
        return modules;
    }
}