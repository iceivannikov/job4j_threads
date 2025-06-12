package ru.job4j.pools;

import java.util.concurrent.CompletableFuture;

public class RolColSum {
    public record Sums(int rowSum, int colSum) {

    }

    public static Sums[] sum(int[][] matrix) {
        if (matrix == null) {
            throw new IllegalArgumentException("Matrix must be square and non-null");
        }
        if (matrix.length == 0 || matrix[0].length == 0) {
            return new Sums[0];
        }
        for (int[] row : matrix) {
            if (row == null || row.length != matrix.length) {
                throw new IllegalArgumentException("Matrix must be square and non-null");
            }
        }
        if (matrix.length > 10) {
            return asyncSum(matrix);
        }
        Sums[] result = new Sums[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            int rowSum = 0;
            int colSum = 0;
            for (int j = 0; j < matrix[i].length; j++) {
                rowSum += matrix[i][j];
                colSum += matrix[j][i];
            }
            result[i] = new Sums(rowSum, colSum);
        }
        return result;
    }

    private static Sums[] asyncSum(int[][] matrix) {
        int size = matrix.length;
        CompletableFuture<Sums>[] futures = new CompletableFuture[size];
        for (int i = 0; i < size; i++) {
            int index = i;
            futures[i] = CompletableFuture.supplyAsync(() -> {
                int rowSum = 0;
                int colSum = 0;
                for (int j = 0; j < size; j++) {
                    rowSum += matrix[index][j];
                    colSum += matrix[j][index];
                }
                return new Sums(rowSum, colSum);
            });
        }
        Sums[] result = new Sums[size];
        for (int i = 0; i < size; i++) {
            result[i] = futures[i].join();
        }
        return result;
    }
}
