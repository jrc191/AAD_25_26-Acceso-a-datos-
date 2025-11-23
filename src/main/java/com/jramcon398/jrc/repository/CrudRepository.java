package com.jramcon398.jrc.repository;

import java.util.List;

/**
 * Generic CRUD repository interface for managing entities.
 *
 * @param <T> the type of the entity
 */
public interface CrudRepository<T> {

    T insert(T entity);

    List<T> findAll();

    T findById(Integer id);

    T update(T entity);

    boolean delete(Integer id);

}