package org.nextoperation.memory.repository;

import java.util.List;

/**
 * Extended repository with multi-value support.
 *
 * @since 1.1.0
 * @author haril song
 * @param <T> element type
 * @param <ID> key type
 */
public interface MultiValueRepository<T, ID> extends MemoryRepository<List<T>, ID> {

    /**
     * Return the first element for the given key.
     *
     * @param id key
     * @return first element
     */
    T findFirst(ID id);

    /**
     * Add element
     *
     * @param id id of element
     * @param element element to save
     */
    void add(ID id, T element);

    default void addAll(ID id, List<? extends T> elements) {
        elements.forEach(e -> add(id, e));
    }

    List<T> findById(ID id);

    List<T> findValues();

    void removeById(ID id);

    void clear();

    void removeAll(List<ID> ids);

    void removeValue(T value);

    void removeValues(List<T> values);

    void update(ID id, List<T> elements);

}
