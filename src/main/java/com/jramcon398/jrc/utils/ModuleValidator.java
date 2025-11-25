package com.jramcon398.jrc.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class ModuleValidator {

    /**
     * Validation methods for Module fields
     */

    public Integer validateId(Integer id) {
        if (id == null) {
            return null;
        }
        if (id < 0) {
            log.warn("Module ID cannot be negative ({}). Setting default value: {}", id, Constants.DEFAULT_MODULE_ID);
            return Constants.DEFAULT_MODULE_ID;
        }
        return id;
    }

    public String validateCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            log.warn("Module code cannot be null or empty. Setting default value: '{}'", Constants.DEFAULT_MODULE_CODE);
            return Constants.DEFAULT_MODULE_CODE;
        }
        return code;
    }

    public String validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            log.warn("Module name cannot be null or empty. Setting default value: '{}'", Constants.DEFAULT_MODULE_NAME);
            return Constants.DEFAULT_MODULE_NAME;
        }
        return name;
    }

    public Integer validateHours(Integer hours) {
        if (hours == null) {
            log.warn("Module hours cannot be null. Setting default value: {}", Constants.DEFAULT_MODULE_HOURS);
            return Constants.DEFAULT_MODULE_HOURS;
        }
        if (hours < 0) {
            log.warn("Module hours cannot be negative ({}). Setting default value: {}", hours, Constants.DEFAULT_MODULE_HOURS);
            return Constants.DEFAULT_MODULE_HOURS;
        }
        return hours;
    }
}