package com.jramcon398.jrc.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Module {

    private int id;
    private String code;
    private String name;
    private int hours;

}
