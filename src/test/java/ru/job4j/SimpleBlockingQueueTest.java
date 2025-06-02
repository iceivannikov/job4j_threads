package ru.job4j;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

import static org.assertj.core.api.Assertions.*;

class SimpleBlockingQueueTest {
    @Test
    void whenSingleProducerAddsElementsThenConsumerReceivesThemInOrder() throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(5);
        Thread producer = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                try {
                    queue.offer(i);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        List<Integer> list = new ArrayList<>();
        Thread consumer = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                try {
                    list.add(queue.poll());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
        assertThat(list).isEqualTo(List.of(1, 2, 3, 4, 5));
    }

    @Test
    void whenQueueIsFullThenProducerWaitsUntilSpaceAvailable() throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(1);
        CountDownLatch producerReady = new CountDownLatch(1);
        CountDownLatch consumerCanGo = new CountDownLatch(1);
        Thread producer = new Thread(() -> {
            try {
                queue.offer(1);
                producerReady.countDown();
                queue.offer(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        List<Integer> list = new ArrayList<>();
        Thread consumer = new Thread(() -> {
            try {
                consumerCanGo.await();
                list.add(queue.poll());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        producer.start();
        consumer.start();
        producerReady.await();
        consumerCanGo.countDown();
        producer.join();
        consumer.join();
        assertThat(list).isEqualTo(List.of(1));
        assertThat(queue.poll()).isEqualTo(2);
    }

    @Test
    void whenQueueIsEmptyThenConsumerWaitsUntilElementAvailable() throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(1);
        CountDownLatch producerCanGo = new CountDownLatch(1);
        CountDownLatch consumerReady = new CountDownLatch(1);
        Thread producer = new Thread(() -> {
            try {
                producerCanGo.await();
                queue.offer(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        List<Integer> list = new ArrayList<>();
        Thread consumer = new Thread(() -> {
            try {
                consumerReady.countDown();
                list.add(queue.poll());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        producer.start();
        consumer.start();
        consumerReady.await();
        producerCanGo.countDown();
        producer.join();
        consumer.join();
        assertThat(list).containsExactly(1);
    }

    @Test
    void whenProducerFinishesThenAllElementsAreConsumed() throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(3);
        Thread producer = new Thread(() -> {
            try {
                queue.offer(1);
                queue.offer(2);
                queue.offer(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        List<Integer> list = new ArrayList<>();
        Thread consumer = new Thread(() -> {
            try {
                list.add(queue.poll());
                list.add(queue.poll());
                list.add(queue.poll());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
        assertThat(list.size()).isEqualTo(3);
        assertThat(list).isEqualTo(List.of(1, 2, 3));
        assertThat(queue.isEmpty()).isTrue();
    }

    @Test
    void whenMultipleOffersAndPollsThenAllElementsAreProcessed() throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(1000);
        Thread producer = new Thread(() -> {
            for (int i = 1; i <= 1000; i++) {
                try {
                    queue.offer(i);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        List<Integer> list = new ArrayList<>();
        Thread consumer = new Thread(() -> {
            for (int i = 1; i <= 1000; i++) {
                try {
                    list.add(queue.poll());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
        assertThat(list.size()).isEqualTo(1000);
        assertThat(queue.isEmpty()).isTrue();
    }

    @Test
    void whenProducerAndConsumerRunSimultaneouslyThenNoDataIsLost() throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(5);
        Thread producer = new Thread(() -> {
            for (int i = 1; i <= 1000; i++) {
                try {
                    queue.offer(i);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        List<Integer> list = new ArrayList<>();
        Thread consumer = new Thread(() -> {
            for (int i = 1; i <= 1000; i++) {
                try {
                    list.add(queue.poll());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
        assertThat(list.size()).isEqualTo(1000);
        assertThat(queue.isEmpty()).isTrue();
    }

    @Test
    public void whenFetchAllThenGetIt() throws InterruptedException {
        final CopyOnWriteArrayList<Integer> buffer = new CopyOnWriteArrayList<>();
        final SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(3);
        Thread producer = new Thread(() -> {
                    for (int i = 0; i < 5; i++) {
                        try {
                            queue.offer(i);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        );
        producer.start();
        Thread consumer = new Thread(() -> {
                    while (!queue.isEmpty() || !Thread.currentThread().isInterrupted()) {
                        try {
                            buffer.add(queue.poll());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Thread.currentThread().interrupt();
                        }
                    }
                }
        );
        consumer.start();
        producer.join();
        consumer.interrupt();
        consumer.join();
        assertThat(buffer).containsExactly(0, 1, 2, 3, 4);
    }

    @Test
    void whenProducerCompletesThenConsumerReceivesAllItems() throws InterruptedException {
        final CopyOnWriteArrayList<Integer> buffer = new CopyOnWriteArrayList<>();
        final SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(1);
        Thread producer = new Thread(() -> {
            try {
                queue.offer(1);
                queue.offer(2);
                queue.offer(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        Thread consumer = new Thread(() -> {
            while (!queue.isEmpty() || !Thread.currentThread().isInterrupted()) {
                try {
                    buffer.add(queue.poll());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        });
        producer.start();
        consumer.start();
        producer.join();
        consumer.interrupt();
        consumer.join();
        assertThat(buffer).containsExactly(1, 2, 3);
        assertThat(queue.isEmpty()).isTrue();
    }
}