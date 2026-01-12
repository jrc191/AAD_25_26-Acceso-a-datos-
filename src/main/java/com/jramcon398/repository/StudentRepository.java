package com.jramcon398.repository;

import com.jramcon398.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByName(String name);

    // Modified query so we search inside email
    @Query("SELECT s FROM Student s WHERE s.email LIKE %:text%")
    List<Student> searchByEmail(@Param("text") String text);
}