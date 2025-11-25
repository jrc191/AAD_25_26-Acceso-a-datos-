package com.jramcon398.jrc.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
@UtilityClass
public class EnrollmentValidator {

    /**
     * Validation methods for Enrollment fields
     */

    public LocalDate validateDate(LocalDate date) {
        if (date == null) {
            log.warn("Enrollment date cannot be null. Setting default: current date");
            return LocalDate.now();
        }
        return date;
    }

    public Integer validateId(Integer id) {
        if (id == null) {
            log.warn("Enrollment ID cannot be null. Setting default: {}", Constants.DEFAULT_ENROLLMENT_ID);
            return Constants.DEFAULT_ENROLLMENT_ID;
        }
        return id;
    }

    public Integer validateStudentId(Integer studentId) {
        if (studentId == null) {
            log.warn("Enrollment studentId cannot be null. Setting default: {}", Constants.DEFAULT_STUDENT_ID);
            return Constants.DEFAULT_STUDENT_ID;
        }
        if (studentId < 0) {
            log.warn("Enrollment studentId cannot be negative ({}). Setting default: {}", studentId, Constants.DEFAULT_STUDENT_ID);
            return Constants.DEFAULT_STUDENT_ID;
        }
        return studentId;
    }

    public Integer validateModuleId(Integer moduleId) {
        if (moduleId == null) {
            log.warn("Enrollment moduleId cannot be null. Setting default: {}", Constants.DEFAULT_MODULE_ID);
            return Constants.DEFAULT_MODULE_ID;
        }
        if (moduleId < 0) {
            log.warn("Enrollment moduleId cannot be negative ({}). Setting default: {}", moduleId, Constants.DEFAULT_MODULE_ID);
            return Constants.DEFAULT_MODULE_ID;
        }
        return moduleId;
    }
}
