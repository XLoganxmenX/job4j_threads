package ru.job4j.pools;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.*;

class RolColSumTest {
    @Test
    public void whenSumOn2X2Matrix() {
        int[][] matrix = {{7, 8},
                          {4, 5}};
        Sums[] expect = {
                new Sums(15, 11),
                new Sums(9, 13)
        };

        Sums[] result = RolColSum.sum(matrix);

        assertThat(result).isEqualTo(expect);
    }

    @Test
    public void whenSumOn3X3Matrix() {
        int[][] matrix = {{7, 8, 9},
                          {4, 5, 6},
                          {1, 2, 3}};
        Sums[] expect = {
                new Sums(24, 12),
                new Sums(15, 15),
                new Sums(6, 18)
        };


        Sums[] result = RolColSum.sum(matrix);

        assertThat(result).isEqualTo(expect);
    }

    @Test
    public void whenAsyncSumOn2X2Matrix() throws ExecutionException, InterruptedException {
        int[][] matrix = {{7, 8},
                          {4, 5}};
        Sums[] expect = {
                new Sums(15, 11),
                new Sums(9, 13)
        };

        Sums[] result = RolColSum.asyncSum(matrix);

        assertThat(result).isEqualTo(expect);
    }

    @Test
    public void whenAsyncSumOn3X3Matrix() throws ExecutionException, InterruptedException {
        int[][] matrix = {{7, 8, 9},
                          {4, 5, 6},
                          {1, 2, 3}};
        Sums[] expect = {
                new Sums(24, 12),
                new Sums(15, 15),
                new Sums(6, 18)
        };

        Sums[] result = RolColSum.asyncSum(matrix);

        assertThat(result).isEqualTo(expect);
    }
}