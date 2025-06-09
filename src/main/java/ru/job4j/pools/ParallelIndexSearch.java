package ru.job4j.pools;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.function.Predicate;

public class ParallelIndexSearch<T> extends RecursiveTask<Integer> {
    private final T[] array;
    private final Predicate<T> predicate;
    private final int from;
    private final int to;
    private static final int THRESHOLD = 10;

    public ParallelIndexSearch(T[] array, Predicate<T> predicate, int from, int to) {
        this.array = array;
        this.predicate = predicate;
        this.from = from;
        this.to = to;
    }

    public static <T> int search(T[] array, Predicate<T> predicate) {
        if (array == null || array.length == 0) {
            return -1;
        }
        Integer result;
        try (ForkJoinPool forkJoinPool = new ForkJoinPool()) {
            result = forkJoinPool.invoke(new ParallelIndexSearch<>(array, predicate, 0, array.length - 1));
        }
        return result == null ? -1 : result;
    }

    @Override
    protected Integer compute() {
        if (from == to) {
            return predicate.test(array[from]) ? from : -1;
        }
        if (to - from <= THRESHOLD) {
            return linearSearch();
        }
        int mid = (from + to) / 2;
        ParallelIndexSearch<T> left = new ParallelIndexSearch<>(array, predicate, from, mid);
        ParallelIndexSearch<T> right = new ParallelIndexSearch<>(array, predicate, mid + 1, to);
        left.fork();
        int rightResult = right.compute();
        int leftResult = left.join();
        return leftResult != -1 ? leftResult : rightResult;
    }

    private int linearSearch() {
        for (int i = from; i <= to; i++) {
            if (predicate.test(array[i])) {
                return i;
            }
        }
        return -1;
    }
}
