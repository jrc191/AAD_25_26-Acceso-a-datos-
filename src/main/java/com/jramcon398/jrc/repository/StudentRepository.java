package com.jramcon398.jrc.repository;

import com.jramcon398.jrc.config.PostgresqlDriver;
import com.jramcon398.jrc.models.Student;
import com.jramcon398.jrc.utils.SQLQueries;
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
     * Inserts student in the database
     *
     * @param student to insert
     * @return Student inserted
     */
    @Override
    public Student insert(Student student) {
        String sql = SQLQueries.Student_Queries.INSERT;

        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, student.getNif());
            ps.setString(2, student.getName());
            ps.setString(3, student.getEmail());

            log.debug("Executing insert for student: {}", student);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int generatedId = rs.getInt("id_alumno");
                    student.setId(generatedId);
                    log.info("Successfully added student with ID {}: {}", generatedId, student);
                    return student;
                } else {
                    log.error("No result set returned from INSERT");
                }
            }

            log.error("Failed to insert student, no ID generated");
            return null;

        } catch (SQLException e) {
            log.error("Error inserting student: {}", e.getMessage());
            throw new RuntimeException("Error inserting student", e);
        }
    }

    /**
     * Lists all students
     *
     * @return List<Student> of all students
     */

    @Override
    public List<Student> findAll() {
        String sql = SQLQueries.Student_Queries.FIND_ALL;
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
     * Finds a student by ID using provided connection (for transactions)
     *
     * @param id   of the student to find
     * @param conn Connection to use
     * @return Student found or null if not found
     */
    public Student findById(Integer id, Connection conn) {
        String sql = SQLQueries.Student_Queries.FIND_BY_ID;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Student student = mapRow(rs);
                    log.info("Found student: {}", student);
                    return student;
                }

                log.warn("Student not found with id: {}", id);
                return null;
            }

        } catch (SQLException e) {
            log.error("Error finding student by id {}: {}", id, e.getMessage());
            throw new RuntimeException("Error finding student by id", e);
        }
    }

    //No transaction version
    @Override
    public Student findById(Integer id) {
        try (Connection conn = postgresqlDriver.getConnection()) {
            return findById(id, conn);
        } catch (SQLException e) {
            log.error("Error getting connection: {}", e.getMessage());
            throw new RuntimeException("Error finding student", e);
        }
    }

    /**
     * Finds a student by NIF.
     *
     * @param nif of the student to find
     * @return Student found or null if not found
     */

    public Student findByNif(String nif) {
        String sql = SQLQueries.Student_Queries.FIND_BY_NIF;

        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nif);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Student student = mapRow(rs);
                    log.warn("Found student by NIF: {}", student);
                    return student;
                }

                log.info("Student not found with NIF: {}", nif);
                return null;
            }

        } catch (SQLException e) {
            log.error("Error finding student by NIF {}: {}", nif, e.getMessage());
            throw new RuntimeException("Error finding student by NIF", e);
        }
    }

    /**
     * Updates a student in the database
     *
     * @param student to update
     * @return Student updated
     */
    @Override
    public Student update(Student student) {
        String sql = SQLQueries.Student_Queries.UPDATE;

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
     * Deletes a student by ID
     *
     * @param id of the student to delete
     * @return true if deleted, false otherwiseº
     */
    @Override
    public boolean delete(Integer id) {
        String sql = SQLQueries.Student_Queries.DELETE;

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
