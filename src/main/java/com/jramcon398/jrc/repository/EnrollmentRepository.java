package com.jramcon398.jrc.repository;

import com.jramcon398.jrc.config.PostgresqlDriver;
import com.jramcon398.jrc.models.Enrollment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class EnrollmentRepository implements CrudRepository<Enrollment> {

    private final PostgresqlDriver postgresqlDriver;

    /**
     * Inserts enrollment using provided connection (for transactions)
     *
     * @param enrollment to insert
     * @param conn       active connection
     * @return Enrollment inserted
     */

    public Enrollment insert(Enrollment enrollment, Connection conn) {
        String sql = "INSERT INTO matricula (id_alumno, id_modulo, fecha) VALUES (?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, enrollment.getStudentId());
            ps.setInt(2, enrollment.getModuleId());
            ps.setDate(3, Date.valueOf(enrollment.getDate()));

            int rowsAffected = ps.executeUpdate();
            log.info("Enrollment created: {} (rows affected: {})", enrollment, rowsAffected);
            return enrollment;

        } catch (SQLException e) {
            log.error("Error creating enrollment: {}", e.getMessage());
            return null;
        }
    }

    // No transaction version
    @Override
    public Enrollment insert(Enrollment enrollment) {
        try (Connection conn = postgresqlDriver.getConnection()) {
            return insert(enrollment, conn);
        } catch (SQLException e) {
            log.error("Error getting connection: {}", e.getMessage());
            throw new RuntimeException("Error creating enrollment", e);
        }
    }

    /**
     * @return List<Enrollment> of all enrollments
     */

    @Override
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

            log.info("Found {} enrollments", enrollments.size());

        } catch (SQLException e) {
            log.error("Error finding all enrollments: {}", e.getMessage());
            throw new RuntimeException("Error finding all enrollments", e);
        }

        return enrollments;
    }

    /**
     * Finds enrollments by student ID.
     *
     * @param studentId
     * @return List<Enrollment>
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
            throw new RuntimeException("Error finding enrollments for student", e);
        }

        return enrollments;
    }

    /**
     * Not applicable for Enrollment. Use findByStudent instead.
     *
     * @param id
     * @return
     */

    @Override
    public Enrollment findById(Integer id) {
        throw new UnsupportedOperationException("Use findByStudent to find enrollments by student ID");
    }

    /**
     * No sense to update an enrollment. It depends on both student and module.
     * Only could update the date, but it's not useful.
     *
     * @param entity
     * @return
     */
    @Override
    public Enrollment update(Enrollment entity) {
        throw new UnsupportedOperationException("Update operation is not supported for Enrollment");
    }

    /**
     * Deletes an enrollment by student ID and module ID.
     * Deletes the record matching both keys.
     *
     * @param studentId
     * @param moduleId
     */

    public void deleteByBothKeys(int studentId, int moduleId) {
        String sql = "DELETE FROM matricula WHERE id_alumno = ? AND id_modulo = ?";

        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.setInt(2, moduleId);
            int rowsAffected = ps.executeUpdate();
            log.info("Enrollment deleted: studentId={}, moduleId={} (rows affected: {})",
                    studentId, moduleId, rowsAffected);

        } catch (SQLException e) {
            log.error("Error deleting enrollment: {}", e.getMessage());
            throw new RuntimeException("Error deleting enrollment", e);
        }
    }

    /**
     * Not applicable for Enrollment. Use deleteByBothKeys instead.
     *
     * @param id
     * @return
     */
    @Override
    public boolean delete(Integer id) {
        throw new UnsupportedOperationException("Use deleteByBothKeys to delete enrollment by student ID and module ID");
    }

    /**
     * Calls the stored procedure to count enrollments for a given student.
     *
     * @param studentId of the student to count enrollments for
     * @return total enrollments
     */
    public int countEnrollments(int studentId) {
        String sql = "{ ? = call count_enrollments(?) }";

        try (Connection con = postgresqlDriver.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setInt(2, studentId);
            cs.execute();
            int total = cs.getInt(1);
            log.info("Total enrollments: {} ", total);
            return total;
        } catch (SQLException e) {
            log.error("Error counting enrollments for student {}: {}", studentId, e.getMessage());
            throw new RuntimeException("Error counting enrollments", e);
        }

    }

    private Enrollment mapRow(ResultSet rs) throws SQLException {
        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(rs.getInt("id_alumno"));
        enrollment.setModuleId(rs.getInt("id_modulo"));
        enrollment.setDate(rs.getDate("fecha").toLocalDate());
        return enrollment;
    }
}