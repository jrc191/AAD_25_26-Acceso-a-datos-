package com.jramcon398.jrc.application;

import org.springframework.stereotype.Service;

/**
 * CustomService interface defining a validation method for entities.
 *
 * @param <T> the type of entity to be validated
 */

@Service
public interface CustomService<T> {

    boolean validate(T entity);

}
