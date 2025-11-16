package com.jramcon398.jrc.repository;

import com.jramcon398.jrc.models.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
public class StudentRepository implements CrudRepository<Student> {


    /**
     * @param s
     * @return
     */
    @Override
    public Student insert(Student s) {
        return null;
    }

    /**
     * @param
     * @return
     */
    @Override
    public List<Student> findAll() {
        return null;
    }

    /**
     * @param id
     * @return
     */
    @Override
    public Student findById(Integer id) {
        if (id != null && id > 0) {
            log.info("Student with ID {} found.", id);
            Student student = new Student();
            student.setId(id);

            return student;
        } else {
            log.error("Failed to get any students with ID: {}", id);
            return null;
        }
    }

    /**
     * @param entity
     * @return
     */
    @Override
    public Student update(Student entity) {
        return null;
    }

    /**
     * @param id
     * @return
     */
    @Override
    public boolean delete(Integer id) {
        if (id != null && id > 0) {
            log.info("Student with ID {} deleted.", id);
            return true;
        } else {
            log.error("Failed to delete student with ID: {}", id);
            return false;
        }

    }
}
