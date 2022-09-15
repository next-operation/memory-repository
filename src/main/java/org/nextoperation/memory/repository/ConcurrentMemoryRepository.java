package org.nextoperation.memory.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Concurrent memory repository. This repository is thread-safe.
 *
 * @param <T>  type of element
 * @param <ID> type of id
 * @author haril song
 */
public abstract class ConcurrentMemoryRepository<T, ID> implements CrudMemoryRepository<T, ID> {

    /**
     * Create an empty repository
     */
    public ConcurrentMemoryRepository() {
    }

    /**
     * data storage. key is index, value is element. this map is thread-safe.
     *
     * @see ConcurrentHashMap
     */
    private final Map<ID, T> map = new ConcurrentHashMap<>();

    @Override
    public void saveAll(List<? extends T> elements) {
        map.putAll(groupBy(elements));
    }

    /**
     * Grouping List with data to be stored in Map
     *
     * @param elements data to save
     * @return A form that can be stored in a map
     */
    protected abstract Map<ID, T> groupBy(List<? extends T> elements);

    /**
     * Find by id
     *
     * @param id id
     * @return Optional of T
     */
    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(map.get(id));
    }

    /**
     * Find all elements in the repository as a map
     *
     * @return a map of id and elements
     */
    @Override
    public Map<ID, T> findAllAsMap() {
        return Collections.unmodifiableMap(map);
    }

    /**
     * Find all elements in the repository as a list
     *
     * @return List of T
     */
    @Override
    public List<T> findAll() {
        return List.copyOf(map.values());
    }

    /**
     * Find all elements by id
     *
     * @param ids ids
     * @return List of T
     */
    @Override
    public List<T> findAllById(Iterable<ID> ids) {
        List<T> result = new ArrayList<>();
        for (ID id : ids) {
            T t = map.get(id);
            result.add(t);
        }
        return result.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * Count elements in the repository
     *
     * @return number of elements
     */
    @Override
    public long count() {
        return map.values().size();
    }

    /**
     * Delete element by id
     *
     * @param id id
     */
    @Override
    public void deleteById(ID id) {
        map.remove(id);
    }

    /**
     * Delete element, if the element is not in the repository, throw NoSuchElementException.
     *
     * @param element element
     */
    @Override
    public void delete(T element) {
        ID id = map.entrySet().stream()
                .filter(entry -> entry.getValue().equals(element))
                .map(Entry::getKey)
                .findFirst().orElseThrow();
        map.remove(id);
    }

    /**
     * Delete elements by iterable of ids.
     *
     * @param ids ids
     */
    @Override
    public void deleteAllById(Iterable<? extends ID> ids) {
        ids.forEach(map::remove);
    }

    /**
     * Delete elements by iterable of elements.
     *
     * @param elements elements
     */
    @Override
    public void deleteAll(Iterable<? extends T> elements) {
        for (T element : elements) {
            List<ID> ids = map.entrySet().stream()
                    .filter(entry -> entry.getValue().equals(element))
                    .map(Entry::getKey)
                    .collect(Collectors.toList());
            deleteAllById(ids);
        }
    }

    /**
     * Delete all elements in the repository.
     */
    @Override
    public void deleteAll() {
        map.clear();
    }

    /**
     * Check if the element is in the repository.
     *
     * @param id id
     * @return true if the element is in the repository, otherwise false.
     */
    @Override
    public boolean existsById(ID id) {
        return map.containsKey(id);
    }
}
