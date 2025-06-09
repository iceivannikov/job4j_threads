package ru.job4j.pools;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ParallelIndexSearchTest {
    @Test
    void whenSearchIntegerArrayThenReturnCorrectIndex() {
        Integer[] array = {5, 3, 7, 9, 1};
        int result = ParallelIndexSearch.search(array, i -> i == 7);
        assertThat(result).isEqualTo(2);
    }

    @Test
    void whenSearchIntegerNotFoundThenReturnMinusOne() {
        Integer[] array = {5, 3, 7, 9, 1};
        int result = ParallelIndexSearch.search(array, i -> i == 100);
        assertThat(result).isEqualTo(-1);
    }

    @Test
    void whenSearchStringArrayThenReturnCorrectIndex() {
        String[] array = {"apple", "banana", "cherry"};
        int result = ParallelIndexSearch.search(array, s -> s.startsWith("b"));
        assertThat(result).isEqualTo(1);
    }

    @Test
    void whenSearchStringNotFoundThenReturnMinusOne() {
        String[] array = {"apple", "banana", "cherry"};
        int result = ParallelIndexSearch.search(array, s -> s.endsWith("z"));
        assertThat(result).isEqualTo(-1);
    }

    @Test
    void whenSearchUserArrayThenReturnCorrectIndex() {
        User[] users = {
                new User(1, "Ivan"),
                new User(2, "Olga"),
                new User(3, "Sergey")
        };
        int result = ParallelIndexSearch.search(users, u -> u.name().equals("Olga"));
        assertThat(result).isEqualTo(1);
    }

    @Test
    void whenSearchUserNotFoundThenReturnMinusOne() {
        User[] users = {
                new User(1, "Ivan"),
                new User(2, "Olga"),
                new User(3, "Sergey")
        };
        int result = ParallelIndexSearch.search(users, u -> u.id() == 99);
        assertThat(result).isEqualTo(-1);
    }

    @Test
    void whenSearchEmptyArrayThenReturnMinusOne() {
        String[] array = {};
        int result = ParallelIndexSearch.search(array, s -> true);
        assertThat(result).isEqualTo(-1);
    }

    @Test
    void whenArrayHasDuplicatesThenReturnFirstMatchIndex() {
        Integer[] array = {1, 3, 5, 3, 7, 3};
        int result = ParallelIndexSearch.search(array, i -> i == 3);
        assertThat(result).isEqualTo(1);
    }

    @Test
    void whenArrayIsLargeThenRecursiveParallelSearchIsUsed() {
        Integer[] array = new Integer[100];
        for (int i = 0; i < array.length; i++) {
            array[i] = i + 1;
        }
        int result = ParallelIndexSearch.search(array, x -> x == 77);
        assertThat(result).isEqualTo(76);
    }
}

record User(int id, String name) {

}