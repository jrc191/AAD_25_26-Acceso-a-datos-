package com.jramcon398.repository;

import com.jramcon398.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // Required for the 'run' method test (JrcApplication.java)
    Optional<Student> findByNif(String nif);

    // Required to fix the error in StudentService (Custom Query Practice) [cite: 1008]
    @Query("SELECT s FROM Student s WHERE s.email LIKE %:text%")
    List<Student> searchByEmail(@Param("text") String text);
}