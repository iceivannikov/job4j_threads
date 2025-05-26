package ru.job4j.synchronizers;

import java.util.concurrent.CountDownLatch;

public class SystemStartup {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);
        new Thread(() -> {
            load("Сервис A", 1000);
            latch.countDown();
        }).start();
        new Thread(() -> {
            load("Сервис B", 1500);
            latch.countDown();
        }).start();
        new Thread(() -> {
            load("Сервис C", 2000);
            latch.countDown();
        }).start();
        System.out.println("Главный поток ждёт, пока все сервисы загрузятся...");
        latch.await();
        System.out.println("Все сервисы загружены. Продолжаем выполнение.");
    }

    private static void load(String name, int delay) {
        try {
            Thread.sleep(delay);
            System.out.println(name + " загружен.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
