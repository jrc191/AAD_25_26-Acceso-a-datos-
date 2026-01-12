package com.jramcon398.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name = "enrollments")
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Student is mandatory")
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @NotNull(message = "Module is mandatory")
    @ManyToOne
    @JoinColumn(name = "module_id")
    private Module module;

    @NotNull
    private LocalDate enrollmentDate = LocalDate.now(); //Default to current date

    private Double finalGrade;
}