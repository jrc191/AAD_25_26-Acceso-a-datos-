package com.jramcon398.jrc.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@Slf4j
@ToString
public class Module {

    private Integer id;
    private String code;
    private String name;
    private Integer hours;

    public Module(Integer id, String code, String name, Integer hours) {
        this.id = validateId(id);
        this.code = validateCode(code);
        this.name = validateName(name);
        this.hours = validateHours(hours);
    }

    private Integer validateId(Integer id) {
        if (id == null) {
            log.error("Module ID cannot be null. Setting default value: 0");
            return 0;
        }
        return id;
    }

    private String validateCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            log.error("Module code cannot be null or empty. Setting default value: 'NO-CODE'");
            return "NO-CODE";
        }
        return code;
    }

    private String validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            log.error("Module name cannot be null or empty. Setting default value: 'NO-NAME'");
            return "NO-NAME";
        }
        return name;
    }

    private Integer validateHours(Integer hours) {
        if (hours == null) {
            log.error("Module hours cannot be null. Setting default value: 0");
            return 0;
        }
        if (hours < 0) {
            log.error("Module hours cannot be negative ({}). Setting default value: 0", hours);
            return 0;
        }
        return hours;
    }

}
