package com.jramcon398.jrc.repository;

import com.jramcon398.jrc.models.Module;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
public class ModuleRepository implements CrudRepository<Module> {

    /**
     * @param s
     * @return
     */
    @Override
    public Module insert(Module s) {
        return null;
    }

    /**
     * @param
     * @return
     */
    @Override
    public List<Module> findAll() {
        return null;
    }

    /**
     * @param id
     * @return
     */
    @Override
    public Module findById(Integer id) {
        return null;
    }

    /**
     * @param entity
     * @return
     */
    @Override
    public Module update(Module entity) {
        return null;
    }


    /**
     * @param id
     * @return
     */
    @Override
    public boolean delete(Integer id) {
        return false;
    }

}
