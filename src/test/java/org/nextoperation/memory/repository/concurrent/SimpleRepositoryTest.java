package org.nextoperation.memory.repository.concurrent;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

class SimpleRepositoryTest {

    private final ConcurrentMemoryRepository<String, Long> repository = new SimpleRepository<>();

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    void groupBy() {
        // given
        List<String> words = List.of("0", "1", "2");

        // when
        Map<Long, String> map = repository.groupBy(words);

        // then
        assertThat(map).containsEntry(0L, words.get(0));
        assertThat(map).containsEntry(1L, words.get(1));
        assertThat(map).containsEntry(2L, words.get(2));
    }

    @TestFactory
    @DisplayName("DynamicTest")
    Stream<DynamicTest> dynamicTestStream() {
        List<String> words = List.of("0", "1", "2");
        return Stream.of(
                dynamicTest("saveAll 이 호출되면 모든 list 가 저장된다.", () -> {
                    repository.saveAll(words);
                    assertThat(repository.count()).isEqualTo(3);
                }),
                dynamicTest("findAll 로 저장되어 있는 모든 객체를 List 로 받을 수 있다.", () -> {
                    List<String> actual = repository.findAll();
                    assertThat(actual).containsExactlyElementsOf(words);
                }),
                dynamicTest("findById 로 특정 element 를 찾을 수 있다.", () -> {
                    String element = repository.findById(0L).orElseThrow();
                    assertThat(element).isEqualTo("0");
                }),
                dynamicTest("findAllById 로 여러 id 를 동시에 검색할 수 있다.", () -> {
                    List<String> actual = repository.findAllById(List.of(1L, 2L));
                    assertThat(actual).containsExactly("1", "2");
                }),
                dynamicTest("deleteById 로 특정 element 를 삭제할 수 있다. (index)", () -> {
                    repository.deleteById(0L);
                    assertThat(repository.findAll()).containsExactly("1", "2");
                }),
                dynamicTest("delete 로 특정 element 를 삭제할 수 있다.", () -> {
                    repository.delete("1");
                    assertThat(repository.findAll()).containsExactly("2");
                }),
                dynamicTest("deleteAll 이 호출되면 repository 가 초기화된다.", () -> {
                    repository.deleteAll();
                    assertThat(repository.count()).isEqualTo(0);
                })
        );
    }

    @Test
    void findAllAsMap() {
        // given
        List<String> words = List.of("0", "1", "2");
        repository.saveAll(words);
        assertThat(repository.findAll()).containsExactlyElementsOf(words);

        // when
        Map<Long, String> map = repository.findAllAsMap();

        // then
        assertThat(map).containsEntry(0L, words.get(0));
        assertThat(map).containsEntry(1L, words.get(1));
        assertThat(map).containsEntry(2L, words.get(2));
    }

    @Test
    void deleteAllById() {
        // given
        List<String> words = List.of("0", "1", "2");
        repository.saveAll(words);
        assertThat(repository.findAll()).containsExactlyElementsOf(words);

        // when
        repository.deleteAllById(List.of(1L, 2L));

        // then
        assertThat(repository.findAll()).containsExactly("0");
    }

    @Test
    void existById() {
        // given
        List<String> words = List.of("0", "1", "2");
        repository.saveAll(words);
        assertThat(repository.findAll()).containsExactlyElementsOf(words);

        // when
        boolean b = repository.existsById(1L);

        // then
        assertThat(b).isTrue();
    }

    @Test
    void deleteAllIterable() {
        // given
        List<String> words = List.of("0", "1", "2");
        repository.saveAll(words);
        assertThat(repository.findAll()).containsExactlyElementsOf(words);

        repository.deleteAll(List.of("0", "1"));

        assertThat(repository.existsById(0L)).isFalse();
        assertThat(repository.existsById(1L)).isFalse();
        assertThat(repository.existsById(2L)).isTrue();
    }
}
