package com.jramcon398.jrc.repository;

import java.util.List;

public interface CrudRepository<T> {

    T insert(T entity);

    List<T> findAll();

    T findById(int id);

    T update(T entity);

    boolean delete(int id);

}