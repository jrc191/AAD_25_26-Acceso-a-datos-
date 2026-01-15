package com.jramcon398.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "modules")
public class Module {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String name;
    private Integer hours;

    @OneToMany(mappedBy = "module")
    @ToString.Exclude
    private List<Enrollment> enrollments = new ArrayList<>();
}