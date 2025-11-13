package com.jramcon398.jrc.repository;

public interface CrudRepository<T> {

    T insert(T entity);

    T findAll();

    T findById(int id);

    T update(T entity);

    boolean delete(int id);

}