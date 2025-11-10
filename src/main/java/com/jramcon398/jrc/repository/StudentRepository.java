package com.jramcon398.jrc.repository;

import com.jramcon398.jrc.model.Student;
import com.jramcon398.jrc.util.UnsafeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;


@Repository
@Slf4j
public class StudentRepository implements CrudRepository<Student> {
    // SQL statements
    private static final String SQL_INSERT = """
            INSERT INTO alumno (nombre, email)
            VALUES (?, ?)
            """;
    private static final String SQL_SELECT_BY_ID = """
            SELECT id_alumno, nombre, email
            FROM alumno
            WHERE id = ?
            """;
    private static final String SQL_UPDATE = """
            UPDATE alumno
            SET nombre = ?, email = ?
            WHERE id_alumno = ?
            """;
    private static final String SQL_DELETE = """
            DELETE FROM alumno
            WHERE id_alumno = ?
            """;
    private static final String SQL_DELETE_WITHOUT_WHERE = """
            DELETE FROM alumno
            """;

    private final DataSource dataSource;

    public StudentRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean deleteAll() {
        //VALIDAMOS CONSULTA
        if (!SQL_DELETE_WITHOUT_WHERE.toUpperCase().contains("WHERE")) {
            Exception exception = new UnsafeException("No se pudo eliminar el WHERE.");
            log.error("No se pudo eliminar el WHERE.");
            log.error(SQL_DELETE_WITHOUT_WHERE);
            log.error("ERROR -> {}", exception.getMessage());
            return false;
        }

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE_WITHOUT_WHERE)) {

            conn.setAutoCommit(false);
            int deleted = ps.executeUpdate();

            log.info("deleteAll OK, rows deleted={}", deleted);
            conn.commit();
            return true;

        } catch (SQLException e) {
            log.error("Error deleting all Students");
            log.error("Code -> {}", e.getErrorCode());
            log.error("Message -> {}", e.getMessage());
            return false;
        }
    }

    public Student create(Student entity) {
        if (entity == null) {
            log.error("Student cannot be null");
            return null;
        }
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            conn.setAutoCommit(false);
            ps.setString(1, entity.getName());
            ps.setString(2, entity.getEmail());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    entity.setDni(keys.getInt(1));
                }
            }
            log.info("create OK: {}", entity);
            conn.commit();
            return entity;
        } catch (SQLException e) {

            log.error("Error creating Student {}", entity.getName());
            log.error("Code -> {}", e.getErrorCode());
            log.error("Message -> {}", e.getMessage());
            return null;
        }
    }


    @Override
    public Student read(Student entity) {
        if (entity == null || entity.getDni() == 0) {
            log.error("Read requires a Student with non-null DNI");
            return null;
        }
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_ID)) {
            ps.setInt(1, entity.getDni());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Student found = mapRow(rs);
                    log.info("read OK: {}", found);
                    return found;

                } else {
                    log.info("read: no student found with DNI={}", entity.getDni());
                    return null;
                }

            }

        } catch (SQLException e) {
            log.error("Error reading Student DNI={}", entity.getDni());
            log.error("Code -> {}", e.getErrorCode());
            log.error("Message -> {}", e.getMessage());
            return null;
        }
    }

    @Override
    public Student update(Student entity) {
        if (entity == null || entity.getDni() == 0) {
            log.error("Update requires a Student with non-null dni");
            return null;
        }
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {
            conn.setAutoCommit(false);
            ps.setString(1, entity.getName());
            ps.setInt(5, entity.getDni());
            int updated = ps.executeUpdate();
            if (updated == 0) {
                log.error("Student not found for update: dni=" + entity.getDni());

                return null;
            }
            log.info("update OK: {}", entity.getName());
            conn.commit();
            return entity;

        } catch (SQLException e) {
            log.error("Error updating Student dni=" + entity.getDni());
            log.error("Code -> {}", e.getErrorCode());
            log.error("Message -> {}", e.getMessage());
            return null;
        }
    }

    @Override
    public boolean delete(Student entity) {
        if (entity == null || entity.getDni() == 0) {
            log.error("delete requires a Student with non-null dni");
            return false;
        }
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {
            ps.setInt(1, entity.getDni());
            int deleted = ps.executeUpdate();
            boolean ok = deleted > 0;
            log.info("delete {} for dni={}", ok ? "OK" : "NOOP", entity.getDni());
            return ok;
        } catch (SQLException e) {
            log.error("Error deleting Student dni=" + entity.getDni());
            log.error("Code -> {}", e.getErrorCode());
            log.error("Message -> {}", e.getMessage());
            return false;
        }
    }

    private Student mapRow(ResultSet rs) throws SQLException {
        Student s = new Student();
        s.setDni(rs.getInt("id_alumno"));
        s.setName(rs.getString("nombre"));
        s.setEmail(rs.getString("email"));

        return s;


    }
}


