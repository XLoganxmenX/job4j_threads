package ru.job4j.pools;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class RolColSum {
    public static Sums[] sum(int[][] matrix) {
        int n = matrix.length;
        Sums[] sums = new Sums[n];
        for (int row = 0; row < n; row++) {
            Sums rowSums = new Sums();
            for (int column = 0; column < n; column++) {
                rowSums.setRowSum(rowSums.getRowSum() + matrix[row][column]);
                rowSums.setColSum(rowSums.getColSum() + matrix[column][row]);
                sums[row] = rowSums;
            }
        }

        return sums;
    }

    public static Sums[] asyncSum(int[][] matrix) throws ExecutionException, InterruptedException {
        int n = matrix.length;
        Sums[] sums = new Sums[n];
        Map<Integer, CompletableFuture<Integer[]>> futures = new HashMap<>();
        for (int row = 0; row < n; row++) {
            futures.put(row, getLineSum(matrix, row, n));
        }

        for (Integer key : futures.keySet()) {
            Sums lineSums = new Sums();
            int rowSum = futures.get(key).get()[0];
            int colSum = futures.get(key).get()[1];
            lineSums.setRowSum(rowSum);
            lineSums.setColSum(colSum);
            sums[key] = lineSums;
        }

        return sums;
    }

    private static CompletableFuture<Integer[]> getLineSum(int[][] matrix, int row, int end) {
        return CompletableFuture.supplyAsync(() -> {
            int rowSum = 0;
            int columnSum = 0;
            for (int column = 0; column < end; column++) {
                rowSum += matrix[row][column];
                columnSum += matrix[column][row];
            }

            return new Integer[]{rowSum, columnSum};
        });
    }
}