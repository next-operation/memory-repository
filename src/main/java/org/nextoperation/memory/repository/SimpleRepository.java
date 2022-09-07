package org.nextoperation.memory.repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A repository with index as key.
 *
 * @author haril song
 */
public class SimpleRepository extends MemoryRepository<String, Long> {

    @Override
    protected Map<Long, String> groupBy(List<? extends String> elements) {
        return IntStream.range(0, elements.size())
                .boxed()
                .collect(Collectors.toMap(i -> (long) i, elements::get, (a, b) -> b));
    }
}
