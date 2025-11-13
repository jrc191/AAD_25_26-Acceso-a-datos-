package com.jramcon398.jrc.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.lang.Module;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Student {

    private int id;
    private String nif;
    private String name;
    private String email;
    private String curse;
    private List<Module> modules;

}
