package ru.job4j;

public class CountBarrier {
    private final Object monitor = this;

    private final int total;

    private int count = 0;

    public CountBarrier(final int total) {
        this.total = total;
    }

    public void count() {
        synchronized (monitor) {
            count++;
            monitor.notifyAll();
        }
    }

    public void await() {
        synchronized (monitor) {
            while (count < total) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public static void main(String[] args) {
        CountBarrier barrier = new CountBarrier(3);
        for (int i = 1; i <= 5; i++) {
            int threadId = i;
            new Thread(() -> {
                log("Поток " + threadId + " вызвал await()");
                barrier.await();
                log("Поток " + threadId + " продолжил выполнение после await()");
            }).start();
        }
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                log("Вызов count() #1");
                barrier.count();

                Thread.sleep(2000);
                log("Вызов count() #2");
                barrier.count();

                Thread.sleep(2000);
                log("Вызов count() #3");
                barrier.count();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    private static void log(String message) {
        System.out.printf("[%s] %s%n", Thread.currentThread().getName(), message);
    }
}
