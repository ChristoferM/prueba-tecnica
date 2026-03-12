package com.fondos.inversion.services;

import java.util.List;
import java.util.Optional;

public interface GenericService<T, ID> {

    List<T> findAll() throws Exception;

    Optional<T> findById(ID id) throws Exception;

    T save(T entity) throws Exception;

    T update(T entity) throws Exception;

    void delete(T entity) throws Exception;

    void deleteById(ID id) throws Exception;

    void validate(T entity) throws Exception;
}
