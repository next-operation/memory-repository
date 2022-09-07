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

public abstract class MemoryRepository<T, ID> implements CrudMemoryRepository<T, ID> {

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

    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(map.get(id));
    }

    @Override
    public Map<ID, T> findAllAsMap() {
        return Collections.unmodifiableMap(map);
    }

    @Override
    public List<T> findAll() {
        return List.copyOf(map.values());
    }

    @Override
    public List<T> findAllById(Iterable<ID> ids) {
        List<T> result = new ArrayList<>();
        for (ID id : ids) {
            T t = map.get(id);
            result.add(t);
        }
        return result.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public long count() {
        return map.values().size();
    }

    @Override
    public void deleteById(ID id) {
        map.remove(id);
    }

    @Override
    public void delete(T element) {
        ID id = map.entrySet().stream()
                .filter(entry -> entry.getValue().equals(element))
                .map(Entry::getKey)
                .findFirst().orElseThrow();
        map.remove(id);
    }

    @Override
    public void deleteAllById(Iterable<? extends ID> ids) {
        ids.forEach(map::remove);
    }

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

    @Override
    public void deleteAll() {
        map.clear();
    }

    @Override
    public boolean existsById(ID id) {
        return map.containsKey(id);
    }
}
