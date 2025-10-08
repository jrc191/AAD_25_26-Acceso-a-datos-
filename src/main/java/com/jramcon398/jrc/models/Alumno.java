package com.jramcon398.jrc.models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

///Clase Alumno: modelo de datos para representar a un alumno

public class Alumno implements Serializable {
    @Getter @Setter
    int id;
    @Getter @Setter
    String nombre;
    @Getter @Setter
    float nota;

    public Alumno(int id, String nombre, float nota) {
        this.id = id;
        this.nombre = nombre;
        this.nota = nota;
    }


}
