package org.nextoperation.memory.repository;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Concurrent multi-value memory repository. This repository is thread-safe.
 *
 * @since 1.1.0
 * @author haril song
 * @param <T>  type of element
 * @param <ID> type of id
 */
public abstract class ConcurrentMultiValueRepository<T, ID> implements MultiValueRepository<T, ID> {

    private final Map<ID, List<T>> map = new ConcurrentHashMap<>();

    @Override
    public T findFirst(ID id) {
        List<T> elements = map.get(id);
        return (elements != null && !elements.isEmpty() ? elements.get(0) : null);
    }

    @Override
    public void add(ID id, T element) {
        List<T> elements = map.computeIfAbsent(id, k -> new ArrayList<>(1));
        elements.add(element);
    }

    @Override
    public List<T> findById(ID id) {
        return map.getOrDefault(id, Collections.emptyList());
    }

    @Override
    public List<T> findValues() {
        return map.values().stream().flatMap(Collection::stream).collect(toList());
    }

    @Override
    public void removeById(ID id) {
        map.remove(id);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public void removeAll(List<ID> ids) {
        ids.forEach(map::remove);
    }

    @Override
    public void removeValue(T value) {
        map.values().forEach(elements -> elements.remove(value));
    }

    @Override
    public void removeValues(List<T> values) {
        map.values().forEach(elements -> elements.removeAll(values));
    }

    @Override
    public void update(ID id, List<T> elements) {
        map.put(id, elements);
    }
}
