package com.jramcon398.repository;

import com.jramcon398.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    long countByStudentId(Long studentId);

    @Query("SELECT e FROM Enrollment e WHERE e.finalGrade >= :minGrade")
    List<Enrollment> findByMinFinalGrade(@Param("minGrade") Double minGrade);
}