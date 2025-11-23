package com.jramcon398.jrc.repository;

import com.jramcon398.jrc.config.PostgresqlDriver;
import com.jramcon398.jrc.models.Student;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class StudentRepository implements CrudRepository<Student> {

    private final PostgresqlDriver postgresqlDriver;

    /**
     * @param student to insert
     * @return Student inserted
     */
    @Override
    public Student insert(Student student) {
        String sql = "INSERT INTO alumno (nif, nombre, email) VALUES (?, ?, ?)";

        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, student.getNif());
            ps.setString(2, student.getName());
            ps.setString(3, student.getEmail());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                student.setId(rs.getInt("id_alumno"));
                log.info("Student inserted: {}", student);
                return student;
            }

        } catch (SQLException e) {
            log.error("Error inserting student: {}", e.getMessage());
            throw new RuntimeException("Error inserting student", e);
        }
        return null;
    }

    /**
     * @return List<Student> of all students
     */

    @Override
    public List<Student> findAll() {
        String sql = "SELECT id_alumno, nif, nombre, email FROM alumno";
        List<Student> students = new ArrayList<>();

        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Student student = mapRow(rs);
                students.add(student);
            }

            log.info("Found {} students", students.size());

        } catch (SQLException e) {
            log.error("Error finding all students: {}", e.getMessage());
            throw new RuntimeException("Error finding all students", e);
        }

        return students;
    }

    /**
     * @param id of the student to find
     * @return Student found
     */
    @Override
    public Student findById(Integer id) {
        String sql = "SELECT id_alumno, nif, nombre, email FROM alumno WHERE id_alumno = ?";

        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Student student = mapRow(rs);
                log.info("Student found: {}", student);
                return student;
            }

        } catch (SQLException e) {
            log.error("Error finding student by id {}: {}", id, e.getMessage());
            throw new RuntimeException("Error finding student by id", e);
        }

        return null;
    }

    /**
     * @param student to update
     * @return Student updated
     */
    @Override
    public Student update(Student student) {
        String sql = "UPDATE alumno SET nif = ?, nombre = ?, email = ? WHERE id_alumno = ?";

        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, student.getNif());
            ps.setString(2, student.getName());
            ps.setString(3, student.getEmail());
            ps.setInt(4, student.getId());

            int rowsAffected = ps.executeUpdate();
            log.info("Student updated: {} (rows affected: {})", student, rowsAffected);
            return student;

        } catch (SQLException e) {
            log.error("Error updating student {}: {}", student.getId(), e.getMessage());
            throw new RuntimeException("Error updating student", e);
        }
    }

    /**
     * @param id of the student to delete
     * @return true if deleted, false otherwiseº
     */
    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM alumno WHERE id_alumno = ?";

        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            log.info("Student deleted: id={} (rows affected: {})", id, rowsAffected);

            return rowsAffected > 0;

        } catch (SQLException e) {
            log.error("Error deleting student {}: {}", id, e.getMessage());
            throw new RuntimeException("Error deleting student", e);
        }
    }

    private Student mapRow(ResultSet rs) throws SQLException {
        Student student = new Student();
        student.setId(rs.getInt("id_alumno"));
        student.setNif(rs.getString("nif"));
        student.setName(rs.getString("nombre"));
        student.setEmail(rs.getString("email"));
        student.setModules(new ArrayList<>());
        return student;
    }
}
