package org.nextoperation.memory.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Interface for generic CRUD operations on a repository for a specific type.
 *
 * @param <T>  type of element
 * @param <ID> type of id
 * @author haril song
 */
public interface CrudMemoryRepository<T, ID> extends MemoryRepository<T, ID> {

    /**
     * Save all elements
     *
     * @param elements elements to save
     */
    void saveAll(List<? extends T> elements);

    /**
     * Find by id
     *
     * @param id id
     * @return Optional of T
     */
    Optional<T> findById(ID id);

    /**
     * Find all elements in the repository as a list
     *
     * @return List of T
     */
    List<T> findAll();

    /**
     * Find all elements by ids
     *
     * @param ids ids to find
     * @return Iterable of T
     */
    Iterable<T> findAllById(Iterable<ID> ids);

    /**
     * Count elements in the repository
     *
     * @return number of elements
     */
    long count();

    /**
     * Delete element by id
     *
     * @param id id
     */
    void deleteById(ID id);

    /**
     * delete element
     *
     * @param element element to delete
     */
    void delete(T element);

    /**
     * Delete All elements by iterable of ids
     *
     * @param ids ids to delete
     */
    void deleteAllById(Iterable<? extends ID> ids);

    /**
     * Delete all elements by iterable of elements
     *
     * @param elements elements to delete
     */
    void deleteAll(Iterable<? extends T> elements);

    /**
     * Delete all elements
     */
    void deleteAll();

    /**
     * Check if element exists by id
     *
     * @param id id
     * @return true if element exists
     */
    boolean existsById(ID id);

    /**
     * Find all elements in the repository as a map
     *
     * @return a map of id and elements
     */
    Map<ID, T> findAllAsMap();

}
