package com.jramcon398.jrc.models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Class Student: data model to represent a student record.
 * Includes id, name, and grade attributes.
 */

public class Student implements Serializable {
    @Getter
    @Setter
    int id;
    @Getter
    @Setter
    String name;
    @Getter
    @Setter
    float grade;

    public Student(int id, String name, float grade) {
        this.id = id;
        this.name = name;
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", grade=" + grade +
                '}';
    }
}