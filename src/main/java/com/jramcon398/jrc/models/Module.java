package com.jramcon398.jrc.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import static com.jramcon398.jrc.utils.ModuleValidator.*;

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
}