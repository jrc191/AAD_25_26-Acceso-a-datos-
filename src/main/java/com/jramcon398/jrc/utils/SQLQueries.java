package com.jramcon398.jrc.utils;

public class SQLQueries {

    public static class EnrollmentQueries {
        public static final String INSERT = "INSERT INTO matricula (id_alumno, id_modulo, fecha) VALUES (?, ?, ?)";
        public static final String FIND_ALL = "SELECT id_alumno, id_modulo, fecha FROM matricula";
        public static final String FIND_BY_STUDENT = "SELECT id_alumno, id_modulo, fecha FROM matricula WHERE id_alumno = ?";
        public static final String DELETE_BY_BOTH_KEYS = "DELETE FROM matricula WHERE id_alumno = ? AND id_modulo = ?";
        public static final String COUNT_ENROLLMENTS = "{ ? = call count_enrollments(?) }";
    }

    public static class Module_Queries {
        public static final String INSERT = "INSERT INTO modulo (codigo, nombre, horas) VALUES (?, ?, ?) RETURNING id_modulo";
        public static final String FIND_ALL = "SELECT id_modulo, codigo, nombre, horas FROM modulo";
        public static final String FIND_BY_ID = "SELECT id_modulo, codigo, nombre, horas FROM modulo WHERE id_modulo = ?";
        public static final String FIND_BY_CODE = "SELECT id_modulo, codigo, nombre, horas FROM modulo WHERE codigo = ?";
        public static final String UPDATE = "UPDATE modulo SET codigo = ?, nombre = ?, horas = ? WHERE id_modulo = ?";
        public static final String DELETE = "DELETE FROM modulo WHERE id_modulo = ?";
    }
    
    public static class Student_Queries {
        public static final String INSERT = "INSERT INTO alumno (nif, nombre, email) VALUES (?, ?, ?) RETURNING id_alumno";
        public static final String FIND_ALL = "SELECT id_alumno, nif, nombre, email FROM alumno";
        public static final String FIND_BY_ID = "SELECT id_alumno, nif, nombre, email FROM alumno WHERE id_alumno = ?";
        public static final String FIND_BY_NIF = "SELECT id_alumno, nif, nombre, email FROM alumno WHERE nif = ?";
        public static final String UPDATE = "UPDATE alumno SET nif = ?, nombre = ?, email = ? WHERE id_alumno = ?";
        public static final String DELETE = "DELETE FROM alumno WHERE id_alumno = ?";
    }
}