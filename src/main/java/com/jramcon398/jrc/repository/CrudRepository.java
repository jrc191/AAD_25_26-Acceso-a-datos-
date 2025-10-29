package com.jramcon398.jrc.repository;

import org.springframework.stereotype.Repository;

/**
 * CrudRepository interface defining basic CRUD operations. Implemented by specific repositories (LogRepository).
 *
 * @param <T> the type of entity to be managed
 */

@Repository
public interface CrudRepository<T> {

    T create(T entity);

    T read(T entity);

    T update(T entity);

    boolean delete(T entity);

}
