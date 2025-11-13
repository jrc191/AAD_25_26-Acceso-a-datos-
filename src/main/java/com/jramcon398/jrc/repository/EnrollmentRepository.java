package com.jramcon398.jrc.repository;

import com.jramcon398.jrc.models.Enrollment;

import java.util.List;

public class EnrollmentRepository implements CrudRepository<Enrollment> {


    /**
     * @param entity
     * @param modules
     * @return
     */
    @Override
    public Enrollment insert(Enrollment entity, List<Module> modules) {
        return null;
    }

    /**
     * @return
     */
    @Override
    public Enrollment findAll() {
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
}
