package com.jramcon398.jrc.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

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

    private LocalDate validateDate(LocalDate date) {
        if (date == null) {
            log.error("Enrollment date cannot be null. Setting default value: current date");
            return LocalDate.now();
        }
        return date;
    }

    private Integer validateId(Integer id) {
        if (id == null) {
            log.error("Enrollment ID cannot be null. Setting default value: 0");
            return 0;
        }
        return id;
    }

    private Integer validateStudentId(Integer studentId) {
        if (studentId == null) {
            log.error("Enrollment studentId cannot be null. Setting default value: 0");
            return 0;
        }
        if (studentId < 0) {
            log.error("Enrollment studentId cannot be negative ({}). Setting default value: 0", studentId);
            return 0;
        }
        return studentId;
    }

    private Integer validateModuleId(Integer moduleId) {
        if (moduleId == null) {
            log.error("Enrollment moduleId cannot be null. Setting default value: 0");
            return 0;
        }
        if (moduleId < 0) {
            log.error("Enrollment moduleId cannot be negative ({}). Setting default value: 0", moduleId);
            return 0;
        }
        return moduleId;
    }
}