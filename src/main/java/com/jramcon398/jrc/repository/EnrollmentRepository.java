package com.jramcon398.jrc.repository;

import com.jramcon398.jrc.config.PostgresqlDriver;
import com.jramcon398.jrc.models.Enrollment;
import com.jramcon398.jrc.models.Module;
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
public class EnrollmentRepository {

    private final PostgresqlDriver postgresqlDriver;

    /**
     * Create enrollment records for a student in multiple modules
     *
     * @param enrollment Enrollment object containing student ID and date
     * @param modules    List of Module objects to enroll the student in
     * @return Enrollment object
     */

    public Enrollment createEnrollment(Enrollment enrollment, List<Module> modules) {
        Connection conn = null;
        try {
            conn = postgresqlDriver.getConnection();
            conn.setAutoCommit(false);

            // enrollment for each module
            String sql = "INSERT INTO matricula (id_alumno, id_modulo, fecha) VALUES (?, ?, ?)";

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                for (Module module : modules) {
                    ps.setInt(1, enrollment.getStudentId());
                    ps.setInt(2, module.getId());
                    ps.setDate(3, java.sql.Date.valueOf(enrollment.getDate()));
                    ps.executeUpdate();
                }
            }

            //All done, commit
            conn.commit();
            log.info("Enrollment created successfully for student {} with {} modules", enrollment.getStudentId(), modules.size());
            return enrollment;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    log.error("Error during rollback: {}", rollbackEx.getMessage());
                }
            }
            log.error("Error creating enrollment: {}", e.getMessage());
            throw new RuntimeException("Error creating enrollment", e);
        } finally {
            if (conn != null) {
                try {
                    //Get back to default
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException closeEx) {
                    log.error("Error closing connection: {}", closeEx.getMessage());
                }
            }
        }
    }

    /**
     * @return List<Enrollment> of all enrollments
     */

    public List<Enrollment> findAll() {
        String sql = "SELECT id_alumno, id_modulo, fecha FROM matricula";
        List<Enrollment> enrollments = new ArrayList<>();

        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Enrollment enrollment = mapRow(rs);
                enrollments.add(enrollment);
            }

            log.info("Found {} enrollment records", enrollments.size());

        } catch (SQLException e) {
            log.error("Error finding all enrollments: {}", e.getMessage());
            throw new RuntimeException("Error finding all enrollments", e);
        }

        return enrollments;
    }

    /**
     * @param studentId of the student to find enrollments for
     * @return List<Enrollment> found
     */

    public List<Enrollment> findByStudent(int studentId) {
        String sql = "SELECT id_alumno, id_modulo, fecha FROM matricula WHERE id_alumno = ?";
        List<Enrollment> enrollments = new ArrayList<>();

        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Enrollment enrollment = mapRow(rs);
                enrollments.add(enrollment);
            }

            log.info("Found {} enrollments for student {}", enrollments.size(), studentId);

        } catch (SQLException e) {
            log.error("Error finding enrollments for student {}: {}", studentId, e.getMessage());
            throw new RuntimeException("Error finding enrollments by student", e);
        }

        return enrollments;
    }

    /**
     * @param studentId of the student
     * @param moduleId  of the module
     * @return boolean indicating if delete was successful
     */

    public boolean delete(int studentId, int moduleId) {
        String sql = "DELETE FROM matricula WHERE id_alumno = ? AND id_modulo = ?";

        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.setInt(2, moduleId);
            int rowsAffected = ps.executeUpdate();
            log.info("Enrollment deleted: student={}, module={} (rows affected: {})", studentId, moduleId, rowsAffected);

            return rowsAffected > 0;

        } catch (SQLException e) {
            log.error("Error deleting enrollment student={}, module={}: {}", studentId, moduleId, e.getMessage());
            throw new RuntimeException("Error deleting enrollment", e);
        }
    }

    /**
     * @param studentId of the student
     * @return int count of enrollments for the student using stored function
     */

    public int countEnrollments(int studentId) {
        String sql = "SELECT count_enrollments(?)";

        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                log.info("Found {} enrollments for student {} using stored function", count, studentId);
                return count;
            }

        } catch (SQLException e) {
            log.error("Error counting enrollments for student {} using stored function: {}", studentId, e.getMessage());
            throw new RuntimeException("Error counting enrollments", e);
        }

        return 0;
    }

    private Enrollment mapRow(ResultSet rs) throws SQLException {
        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(rs.getInt("id_alumno"));
        enrollment.setModuleId(rs.getInt("id_modulo"));
        enrollment.setDate(rs.getDate("fecha").toLocalDate());
        return enrollment;
    }
}