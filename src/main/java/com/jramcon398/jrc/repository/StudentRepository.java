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
    public Student findById(int id) {
        return null;
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
    public boolean delete(int id) {
        return false;
    }
}
