package org.nextoperation.memory.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CrudMemoryRepository<T, ID> {

    void saveAll(List<? extends T> elements);

    Optional<T> findById(ID id);

    List<T> findAll();

    Iterable<T> findAllById(Iterable<ID> ids);

    long count();

    void deleteById(ID id);

    void delete(T element);

    void deleteAllById(Iterable<? extends ID> ids);

    void deleteAll(Iterable<? extends T> elements);

    void deleteAll();

    boolean existsById(ID id);

    Map<ID, T> findAllAsMap();

}
