package com.jramcon398.jrc.repository;

import com.jramcon398.jrc.models.Enrollment;

import java.util.List;

public class EnrollmentRepository implements CrudRepository<Enrollment> {

    public Enrollment createEnrollment(Enrollment e, List<Module> modules) {
        return null;
    }

    /**
     * @param entity
     * @return
     */
    @Override
    public Enrollment insert(Enrollment entity) {
        return null;
    }

    /**
     * @return
     */
    @Override
    public List<Enrollment> findAll() {
        return null;
    }

    /**
     * @param id
     * @return
     */
    @Override
    public Enrollment findById(int id) {
        return null;
    }

    /**
     * @param entity
     * @return
     */
    @Override
    public Enrollment update(Enrollment entity) {
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

    public int countEnrollments(int studentId) {
        

        return 0;
    }
}
