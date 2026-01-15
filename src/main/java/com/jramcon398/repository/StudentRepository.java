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

    // Optional so we can handle the case where no student is found
    Optional<Student> findByNif(String nif);
    
    @Query("SELECT s FROM Student s WHERE s.email LIKE %:text%")
    List<Student> searchByEmail(@Param("text") String text);
}