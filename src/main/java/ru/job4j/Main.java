package ru.job4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(1);

        pool.submit(() -> {
            try {
                System.out.println("Начало работы задачи");
                Thread.sleep(3000);
                System.out.println("Задача завершена");
            } catch (InterruptedException e) {
                System.out.println("Задача была прервана");
            }
        });

        pool.shutdown();

        try {
            if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
                pool.shutdownNow();
                System.out.println("Задача не успела завершиться за 5 секунд, принудительная остановка");
            } else {
                System.out.println("Все задачи завершились вовремя");
            }
        } catch (InterruptedException e) {
            System.out.println("Поток ожидания был прерван");
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }

        System.out.println("Программа завершена");
    }
}
