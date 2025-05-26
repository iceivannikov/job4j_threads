package ru.job4j.synchronizers;

import java.util.concurrent.Phaser;

public class DataProcessor {
    public static void main(String[] args) {
        Phaser phaser = new Phaser(1);
        for (int i = 1; i <= 3; i++) {
            phaser.register();
            int id = i;
            new Thread(() -> {
                System.out.println("Поток " + id + " — фаза 0: подготовка");
                phaser.arriveAndAwaitAdvance();

                System.out.println("Поток " + id + " — фаза 1: обработка");
                phaser.arriveAndAwaitAdvance();

                System.out.println("Поток " + id + " — фаза 2: завершение");
                phaser.arriveAndDeregister();
            }).start();
        }
        while (phaser.getRegisteredParties() > 1) {
            int phase = phaser.getPhase();
            phaser.arriveAndAwaitAdvance();
            System.out.println("Фаза " + phase + " завершена.\n");
        }
        phaser.arriveAndDeregister();
        System.out.println("Все потоки завершили работу.");
    }
}
