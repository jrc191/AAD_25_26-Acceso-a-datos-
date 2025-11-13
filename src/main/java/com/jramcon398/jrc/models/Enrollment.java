package com.jramcon398.jrc.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Enrollment {
    LocalDate date;
    private int id;
    private int studentId;
    private int moduleId;

}
