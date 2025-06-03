package ru.job4j;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CASCountTest {
    @Test
    void whenManyThreadsIncrementThenValueIsCorrect() throws InterruptedException {
        int threads = 100;
        int incrementsPerThread = 1000;
        int expected = threads * incrementsPerThread;
        CASCount counter = new CASCount();
        Thread[] workers = new Thread[threads];
        for (int i = 0; i < threads; i++) {
            workers[i] = new Thread(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    counter.increment();
                }
            });
            workers[i].start();
        }
        for (Thread thread : workers) {
            thread.join();
        }
        assertThat(counter.get()).isEqualTo(expected);
    }
}