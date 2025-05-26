package ru.job4j.synchronizers;

import java.util.concurrent.Semaphore;

public class Shop {
    private static final Semaphore CASHBOXES = new Semaphore(3);

    public static void main(String[] args) {
        for (int i = 1; i <= 10; i++) {
            int customerId = i;
            new Thread(() -> {
                try {
                    System.out.println("Покупатель " + customerId + " ждёт кассу");
                    CASHBOXES.acquire();
                    System.out.println("Покупатель " + customerId + " обслуживается");

                    Thread.sleep(2000);

                    System.out.println("Покупатель " + customerId + " завершил обслуживание");
                    CASHBOXES.release();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }
    }
}
