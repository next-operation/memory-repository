package org.nextoperation.memory.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SimpleMultiValueRepositoryTest {

    @Test
    @DisplayName("add and find")
    void addTest() {
        SimpleMultiValueRepository<String> repository = new SimpleMultiValueRepository<>();
        repository.add(1L, "a");
        repository.add(1L, "b");
        repository.add(1L, "c");
        repository.add(2L, "d");
        repository.add(2L, "e");
        repository.add(2L, "f");
        repository.add(3L, "g");
        repository.add(3L, "h");
        repository.add(3L, "i");
        assertThat(repository.findById(1L)).containsExactly("a", "b", "c");
        assertThat(repository.findById(2L)).containsExactly("d", "e", "f");
        assertThat(repository.findById(3L)).containsExactly("g", "h", "i");
    }

    @Test
    @DisplayName("addAll")
    void addAllTest() {
        SimpleMultiValueRepository<String> repository = new SimpleMultiValueRepository<>();
        repository.addAll(1L, List.of("a", "b", "c"));
        repository.addAll(2L, List.of("d", "e", "f"));
        repository.addAll(3L, List.of("g", "h", "i"));
        assertThat(repository.findById(1L)).containsExactly("a", "b", "c");
        assertThat(repository.findById(2L)).containsExactly("d", "e", "f");
        assertThat(repository.findById(3L)).containsExactly("g", "h", "i");
    }

    @Test
    @DisplayName("findFirst")
    void findFirstTest() {
        SimpleMultiValueRepository<String> repository = new SimpleMultiValueRepository<>();
        repository.add(1L, "a");
        repository.add(1L, "b");
        repository.add(1L, "c");
        repository.add(2L, "d");
        repository.add(2L, "e");
        repository.add(2L, "f");
        repository.add(3L, "g");
        repository.add(3L, "h");
        repository.add(3L, "i");
        assertThat(repository.findFirst(1L)).isEqualTo("a");
        assertThat(repository.findFirst(2L)).isEqualTo("d");
        assertThat(repository.findFirst(3L)).isEqualTo("g");
    }

    @Test
    @DisplayName("findValues")
    void findValuesTest() {
        SimpleMultiValueRepository<String> repository = new SimpleMultiValueRepository<>();
        repository.addAll(1L, List.of("a", "b", "c"));
        repository.addAll(2L, List.of("d", "e", "f"));
        repository.addAll(3L, List.of("g", "h", "i"));
        assertThat(repository.findValues()).containsExactly("a", "b", "c", "d", "e", "f", "g", "h", "i");
    }

    @Test
    @DisplayName("remove")
    void removeTest() {
        SimpleMultiValueRepository<String> repository = new SimpleMultiValueRepository<>();
        repository.addAll(1L, List.of("a", "b", "c"));
        repository.addAll(2L, List.of("d", "e", "f"));
        repository.addAll(3L, List.of("g", "h", "i"));
        repository.removeById(1L);
        repository.removeById(2L);
        repository.removeById(3L);
        assertThat(repository.findValues()).isEmpty();
    }

    @Test
    @DisplayName("removeAll")
    void removeAllTest() {
        SimpleMultiValueRepository<String> repository = new SimpleMultiValueRepository<>();
        repository.addAll(1L, List.of("a", "b", "c"));
        repository.addAll(2L, List.of("d", "e", "f"));
        repository.addAll(3L, List.of("g", "h", "i"));
        repository.removeAll(List.of(1L, 2L, 3L));
        assertThat(repository.findValues()).isEmpty();
    }

    @Test
    @DisplayName("removeValue")
    void removeValueTest() {
        SimpleMultiValueRepository<String> repository = new SimpleMultiValueRepository<>();
        repository.addAll(1L, List.of("a", "b", "c"));
        repository.addAll(2L, List.of("d", "e", "f"));
        repository.addAll(3L, List.of("g", "h", "i"));
        repository.removeValue("a");
        repository.removeValue("d");
        repository.removeValue("g");
        assertThat(repository.findValues()).containsExactly("b", "c", "e", "f", "h", "i");
    }

    @Test
    @DisplayName("removeValues")
    void removeValuesTest() {
        SimpleMultiValueRepository<String> repository = new SimpleMultiValueRepository<>();
        repository.addAll(1L, List.of("a", "b", "c"));
        repository.addAll(2L, List.of("d", "e", "f"));
        repository.addAll(3L, List.of("g", "h", "i"));
        repository.removeValues(List.of("a", "d", "g"));
        assertThat(repository.findValues()).containsExactly("b", "c", "e", "f", "h", "i");
    }

    @Test
    @DisplayName("update")
    void updateTest() {
        SimpleMultiValueRepository<String> repository = new SimpleMultiValueRepository<>();
        repository.addAll(1L, List.of("a", "b", "c"));
        repository.addAll(2L, List.of("d", "e", "f"));
        repository.addAll(3L, List.of("g", "h", "i"));
        repository.update(1L, List.of("a", "b", "c", "d", "e", "f"));
        repository.update(2L, List.of("g", "h", "i", "j", "k", "l"));
        repository.update(3L, List.of("m", "n", "o", "p", "q", "r"));
        assertThat(repository.findValues()).containsExactly("a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
                "k", "l", "m", "n", "o", "p", "q", "r");
    }

    @Test
    @DisplayName("clear")
    void clearTest() {
        SimpleMultiValueRepository<String> repository = new SimpleMultiValueRepository<>();
        repository.addAll(1L, List.of("a", "b", "c"));
        repository.addAll(2L, List.of("d", "e", "f"));
        repository.addAll(3L, List.of("g", "h", "i"));
        repository.clear();
        assertThat(repository.findValues()).isEmpty();
    }

}
