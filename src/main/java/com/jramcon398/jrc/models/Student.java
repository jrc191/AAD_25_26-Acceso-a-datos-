package com.jramcon398.jrc.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static com.jramcon398.jrc.utils.StudentValidator.*;

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

    //With no modules
    public Student(Integer id, String nif, String name, String email, String course) {
        this.id = validateId(id);
        this.nif = validateNif(nif);
        this.name = validateName(name);
        this.email = validateEmail(email);
        this.course = validateCourse(course);
        this.modules = new ArrayList<>();
    }

    //All fields. Cannot use allargscontructor because of validations
    public Student(Integer id, String nif, String name, String email, String course, List<Module> modules) {
        this.id = validateId(id);
        this.nif = validateNif(nif);
        this.name = validateName(name);
        this.email = validateEmail(email);
        this.course = validateCourse(course);
        this.modules = validateModules(modules);
    }

}