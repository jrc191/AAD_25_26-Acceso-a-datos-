package com.jramcon398.repository;

import com.jramcon398.models.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository with some other custom queries for Enrollment entity.
 */

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    // All enrollments from stud
    List<Enrollment> findByStudentId(Long studentId);

    // All enrollments from module
    List<Enrollment> findByModuleId(Long moduleId);

    // Filter by minimum final grade
    @Query("SELECT e FROM Enrollment e WHERE e.finalGrade >= :grade ORDER BY e.finalGrade DESC")
    List<Enrollment> findByMinFinalGrade(@Param("grade") Double grade);

    // Filter by student name and minimum final grade
    @Query("SELECT e FROM Enrollment e WHERE e.student.name = :name AND e.finalGrade >= :minGrade")
    List<Enrollment> findByStudentNameAndMinGrade(@Param("name") String name, @Param("minGrade") Double minGrade);
}