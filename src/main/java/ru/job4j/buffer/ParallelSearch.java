package ru.job4j.buffer;

import ru.job4j.pool.SimpleBlockingQueue;

public class ParallelSearch {
    public static void main(String[] args) throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(1);
        final Thread consumer = new Thread(
                () -> {
                    int count = 0;
                    while (count != -1) {
                        try {
                            count = queue.poll();
                            if (count != -1) {
                                System.out.println(count);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Thread.currentThread().interrupt();
                        }
                    }
                }
        );
        consumer.start();
        new Thread(
                () -> {
                    for (int index = 0; index != 3; index++) {
                        try {
                            queue.offer(index);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        queue.offer(-1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

        ).start();
    }
}
