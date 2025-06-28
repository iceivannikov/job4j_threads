package ru.job4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class CallableTask {
    public static void main(String[] args) {
        FutureTask<Long> task1 = new FutureTask<>(() -> {
            long sum = 0;
            for (int i = 1; i <= 500; i++) {
                sum += i;
                Thread.sleep(1);
            }
            System.out.printf("Задача %d-%d завершена%n", 1, 500);
            return sum;
        });

        FutureTask<Long> task2 = new FutureTask<>(() -> {
            long sum = 0;
            for (int i = 501; i <= 1000; i++) {
                sum += i;
                Thread.sleep(1);
            }
            System.out.printf("Задача %d-%d завершена%n", 501, 1000);
            return sum;
        });

        FutureTask<Long> task3 = new FutureTask<>(() -> {
            long sum = 0;
            for (int i = 1001; i <= 1500; i++) {
                sum += i;
                Thread.sleep(5);
            }
            System.out.printf("Задача %d-%d завершена%n", 1001, 1500);
            return sum;
        });
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        executorService.submit(task1);
        executorService.submit(task2);
        executorService.submit(task3);

        List<FutureTask<Long>> tasks = List.of(task1, task2, task3);

        List<Long> results = new ArrayList<>();

        for (FutureTask<Long> task : tasks) {
            try {
                Long res = task.get(2, TimeUnit.SECONDS);
                results.add(res);
            } catch (TimeoutException e) {
                System.out.println("Задача заняла слишком много времени. Отмена...");
                task.cancel(true);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        executorService.shutdown();

    }
}
