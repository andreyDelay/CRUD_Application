package com.andrey.crud.repository;


import com.andrey.crud.exeptions.ReadFileException;
import com.andrey.crud.exeptions.WriteFileException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface GenericRepository<T, ID> {

    T save(T obj) throws WriteFileException, ReadFileException;

    Optional<T> find(ID id) throws ReadFileException;

    Map<ID,T> findAll() throws ReadFileException;

    boolean saveAll(List<T> list) throws WriteFileException;

    T update(ID id, T newValue) throws ReadFileException, WriteFileException;

    T delete(ID id) throws ReadFileException, WriteFileException;

}
