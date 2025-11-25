package com.jramcon398.jrc.repository;

import com.jramcon398.jrc.models.Enrollment;
import com.jramcon398.jrc.utils.SQLQueries;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class EnrollmentRepository implements CrudRepository<Enrollment> {

    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall countEnrollmentsFn;

    /**
     * Inserts enrollment using provided connection (for transactions)
     *
     * @param enrollment to insert
     * @return Enrollment inserted
     */

    @Override
    public Enrollment insert(Enrollment enrollment) {
        try {
            int rowsAffected = jdbcTemplate.update(
                    SQLQueries.EnrollmentQueries.INSERT,
                    enrollment.getStudentId(),
                    enrollment.getModuleId(),
                    enrollment.getDate()
            );

            if (rowsAffected > 0) {
                log.info("Enrollment created: {} (rows affected: {})", enrollment, rowsAffected);
                return enrollment;
            } else {
                log.error("Failed to create enrollment: {}", enrollment);
                return null;
            }
        } catch (DataIntegrityViolationException ex) {

            log.error("Error inserting enrollment: {}", ex.getMostSpecificCause().getMessage());
            return null;

        }
    }

    /**
     * Find all enrollments.
     *
     * @return List<Enrollment> of all enrollments
     */

    @Override
    public List<Enrollment> findAll() {
        return jdbcTemplate.query(
                SQLQueries.EnrollmentQueries.FIND_ALL,
                (rs, rowNum) -> new Enrollment(
                        rs.getDate("fecha").toLocalDate(),
                        rs.getInt("id_alumno"),
                        rs.getInt("id_modulo")
                )
        );
    }

    /**
     * Finds enrollments by student ID.
     *
     * @param studentId of the student
     * @return List<Enrollment>
     */

    public List<Enrollment> findByStudentId(Integer studentId) {
        return jdbcTemplate.query(
                SQLQueries.EnrollmentQueries.FIND_BY_STUDENT,
                (rs, rowNum) -> new Enrollment(
                        rs.getDate("fecha").toLocalDate(),
                        rs.getInt("id_alumno"),
                        rs.getInt("id_modulo")
                ),
                studentId
        );
    }

    /**
     * Not applicable for Enrollment. Use findByStudent instead.
     *
     * @param id of the enrollment
     * @return Exception
     */

    @Override
    public Enrollment findById(Integer id) {
        throw new UnsupportedOperationException("Use findByStudent to find enrollments by student ID");
    }

    /**
     * No sense to update an enrollment. It depends on both student and module.
     * Only could update the date, but it's not useful.
     *
     * @param enrollment to update
     * @return Enrollment updated
     */
    @Override
    public Enrollment update(Enrollment enrollment) {
        throw new UnsupportedOperationException("Update operation is not supported for Enrollment");
    }

    /**
     * Deletes an enrollment by student ID and module ID.
     * Deletes the record matching both keys.
     *
     * @param studentId of the student
     * @param moduleId  of the module
     * @return boolean indicating success
     *
     */

    public Boolean delete(int studentId, int moduleId) {
        int rowsAffected = jdbcTemplate.update(
                SQLQueries.EnrollmentQueries.DELETE_BY_BOTH_KEYS,
                studentId,
                moduleId
        );

        if (rowsAffected > 0) {
            log.info("Enrollment deleted: studentId={}, moduleId={} (rows affected: {})",
                    studentId, moduleId, rowsAffected);
            return true;
        } else {
            log.warn("No enrollment found for studentId={}, moduleId={}", studentId, moduleId);
            return false;
        }
    }

    /**
     * Not applicable for Enrollment. Use delete (student_id, module_id) instead.
     *
     * @param id of the enrollment to delete
     * @return boolean indicating success
     */
    @Override
    public boolean delete(Integer id) {
        throw new UnsupportedOperationException("Use delete(student_id, module_id) to delete enrollment by student ID and module ID");
    }

    /**
     * Calls the stored procedure to count enrollments for a given student.
     *
     * @param studentId of the student to count enrollments for
     * @return total enrollments
     */
    public int countEnrollments(int studentId) {
        if (countEnrollmentsFn == null) {
            countEnrollmentsFn = new SimpleJdbcCall(jdbcTemplate)
                    .withFunctionName("count_enrollments");
        }

        MapSqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_id_alumno", studentId);

        Integer result = countEnrollmentsFn.executeFunction(Integer.class, in);
        int count = (result == null ? 0 : result); // Handle null case

        log.info("Total enrollments: {}", count);
        return count;

    }

}