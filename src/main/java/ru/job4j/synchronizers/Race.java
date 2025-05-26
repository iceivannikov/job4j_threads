package ru.job4j.synchronizers;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Race {
    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(4, () -> System.out.println("Все готовы. СТАРТ!"));
        for (int i = 1; i <= 4; i++) {
            int id = i;
            new Thread(() -> {
                try {
                    System.out.println("Участник " + id + " готовится...");
                    Thread.sleep((long) (Math.random() * 3000));
                    System.out.println("Участник " + id + " готов.");
                    barrier.await();
                    System.out.println("Участник " + id + " побежал!");
                } catch (InterruptedException | BrokenBarrierException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }
    }
}
