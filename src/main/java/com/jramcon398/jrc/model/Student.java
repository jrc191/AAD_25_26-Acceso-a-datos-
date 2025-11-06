package com.jramcon398.jrc.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@ToString
public class Student extends Person {

    private String curse;
    private List<Module> modules;

    public Student(int dni, String name, String email) {
        super(dni, name, email);
    }

    public Student(int dni, String name, String email, String curse) {
        super(dni, name, email);
        this.curse = curse;
    }

}
