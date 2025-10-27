package com.jramcon398.jrc.application;


public interface CustomService<T> {

    boolean validate(T entity);

}
