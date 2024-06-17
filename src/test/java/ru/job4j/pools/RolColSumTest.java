package ru.job4j.pools;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.*;

class RolColSumTest {
    @Test
    public void whenSumOn2X2Matrix() {
        int[][] matrix = {{7, 8},
                          {4, 5}};

        RolColSum.Sums[] result = RolColSum.sum(matrix);

        assertThat(result[0].getRowSum()).isEqualTo(15);
        assertThat(result[0].getColSum()).isEqualTo(11);

        assertThat(result[1].getRowSum()).isEqualTo(9);
        assertThat(result[1].getColSum()).isEqualTo(13);
    }

    @Test
    public void whenSumOn3X3Matrix() {
        int[][] matrix = {{7, 8, 9},
                          {4, 5, 6},
                          {1, 2, 3}};

        RolColSum.Sums[] result = RolColSum.sum(matrix);

        assertThat(result[0].getRowSum()).isEqualTo(24);
        assertThat(result[0].getColSum()).isEqualTo(12);

        assertThat(result[1].getRowSum()).isEqualTo(15);
        assertThat(result[1].getColSum()).isEqualTo(15);

        assertThat(result[2].getRowSum()).isEqualTo(6);
        assertThat(result[2].getColSum()).isEqualTo(18);
    }

    @Test
    public void whenAsyncSumOn2X2Matrix() throws ExecutionException, InterruptedException {
        int[][] matrix = {{7, 8},
                          {4, 5}};

        RolColSum.Sums[] result = RolColSum.asyncSum(matrix);

        assertThat(result[0].getRowSum()).isEqualTo(15);
        assertThat(result[0].getColSum()).isEqualTo(11);

        assertThat(result[1].getRowSum()).isEqualTo(9);
        assertThat(result[1].getColSum()).isEqualTo(13);
    }

    @Test
    public void whenAsyncSumOn3X3Matrix() throws ExecutionException, InterruptedException {
        int[][] matrix = {{7, 8, 9},
                          {4, 5, 6},
                          {1, 2, 3}};

        RolColSum.Sums[] result = RolColSum.asyncSum(matrix);

        assertThat(result[0].getRowSum()).isEqualTo(24);
        assertThat(result[0].getColSum()).isEqualTo(12);

        assertThat(result[1].getRowSum()).isEqualTo(15);
        assertThat(result[1].getColSum()).isEqualTo(15);

        assertThat(result[2].getRowSum()).isEqualTo(6);
        assertThat(result[2].getColSum()).isEqualTo(18);
    }
}