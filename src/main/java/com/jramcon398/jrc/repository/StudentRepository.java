package com.jramcon398.jrc.repository;

import com.jramcon398.jrc.models.Student;
import com.jramcon398.jrc.utils.SQLQueries;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class StudentRepository implements CrudRepository<Student> {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Inserts student in the database
     *
     * @param student to insert
     * @return Student inserted
     */
    @Override
    public Student insert(Student student) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    SQLQueries.Student_Queries.INSERT,
                    new String[]{"id_alumno"}
            );
            ps.setString(1, student.getNif());
            ps.setString(2, student.getName());
            ps.setString(3, student.getEmail());
            return ps;
        }, keyHolder);

        Integer generatedId = keyHolder.getKey().intValue();
        student.setId(generatedId);

        log.info("Successfully added student with ID {}: {}", generatedId, student);
        return student;
    }

    /**
     * Lists all students
     *
     * @return List<Student> of all students
     */

    @Override
    public List<Student> findAll() {
        return jdbcTemplate.query(
                SQLQueries.Student_Queries.FIND_ALL,
                (rs, rowNum) -> new Student(
                        rs.getInt("id_alumno"),
                        rs.getString("nif"),
                        rs.getString("nombre"),
                        rs.getString("email"),
                        null // course is not retrieved in this query
                )
        );
    }

    /**
     * Finds a student by ID using provided connection (for transactions)
     *
     * @param id of the student to find
     * @return Student found or null if not found
     */
    public Student findById(Integer id) {
        try {
            Student student = jdbcTemplate.queryForObject(
                    SQLQueries.Student_Queries.FIND_BY_ID,
                    (rs, rowNum) -> new Student(
                            rs.getInt("id_alumno"),
                            rs.getString("nif"),
                            rs.getString("nombre"),
                            rs.getString("email"),
                            null
                    ),
                    id
            );
            log.info("Found student: {}", student);
            return student;
        } catch (EmptyResultDataAccessException e) {
            log.warn("Student not found with id: {}", id);
            return null;
        }
    }

    /**
     * Finds a student by NIF.
     *
     * @param nif of the student to find
     * @return Student found or null if not found
     */

    public Student findByNif(String nif) {
        try {
            Student student = jdbcTemplate.queryForObject(
                    SQLQueries.Student_Queries.FIND_BY_NIF,
                    (rs, rowNum) -> new Student(
                            rs.getInt("id_alumno"),
                            rs.getString("nif"),
                            rs.getString("nombre"),
                            rs.getString("email"),
                            null
                    ),
                    nif
            );
            log.info("Found student with NIF {}: {}", nif, student);
            return student;
        } catch (EmptyResultDataAccessException e) {
            log.info("Student not found with NIF: {}", nif);
            return null;
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
        int rowsAffected = jdbcTemplate.update(
                SQLQueries.Student_Queries.UPDATE,
                student.getNif(),
                student.getName(),
                student.getEmail(),
                student.getId()
        );

        if (rowsAffected > 0) {
            log.info("Student updated: {} (rows affected: {})", student, rowsAffected);
            return student;
        } else {
            log.warn("No student found with id: {}", student.getId());
            return null;
        }
    }

    /**
     * Deletes a student by ID
     *
     * @param id of the student to delete
     * @return true if deleted, false otherwise
     */
    @Override
    public boolean delete(Integer id) {
        int rowsAffected = jdbcTemplate.update(
                SQLQueries.Student_Queries.DELETE,
                id
        );

        if (rowsAffected > 0) {
            log.info("Student deleted: id={} (rows affected: {})", id, rowsAffected);
            return true;
        } else {
            log.warn("No student found with id: {}", id);
            return false;
        }
    }

}
