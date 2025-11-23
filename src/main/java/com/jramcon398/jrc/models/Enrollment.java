package com.jramcon398.jrc.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

import static com.jramcon398.jrc.utils.EnrollmentValidator.*;

@Data
@NoArgsConstructor
@Slf4j
@ToString
public class Enrollment {

    private LocalDate date;
    private Integer id;
    private Integer studentId;
    private Integer moduleId;

    // Constructor with validation
    public Enrollment(LocalDate date, Integer id, Integer studentId, Integer moduleId) {
        this.date = validateDate(date);
        this.id = validateId(id);
        this.studentId = validateStudentId(studentId);
        this.moduleId = validateModuleId(moduleId);
    }


}