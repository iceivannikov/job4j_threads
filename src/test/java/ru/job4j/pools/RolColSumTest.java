package ru.job4j.pools;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class RolColSumTest {

    @Test
    void whenSmallMatrixThenSumReturnsExpectedValues() {
        int[][] matrix = {
                {1, 2},
                {3, 4}
        };
        RolColSum.Sums[] result = RolColSum.sum(matrix);
        assertThat(result).hasSize(2);
        assertThat(result[0].rowSum()).isEqualTo(3);
        assertThat(result[0].colSum()).isEqualTo(4);
        assertThat(result[1].rowSum()).isEqualTo(7);
        assertThat(result[1].colSum()).isEqualTo(6);
    }

    @Test
    void whenLargeMatrixThenSumReturnsSameAsManualCalculation() {
        int size = 20;
        int[][] matrix = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = 1;
            }
        }
        RolColSum.Sums[] result = RolColSum.sum(matrix);
        assertThat(result).hasSize(size);
        for (RolColSum.Sums sums : result) {
            assertThat(sums.rowSum()).isEqualTo(size);
            assertThat(sums.colSum()).isEqualTo(size);
        }
    }

    @Test
    void whenSingleElementMatrixThenRowAndColEqualToElement() {
        int[][] matrix = {
                {33}
        };
        RolColSum.Sums[] result = RolColSum.sum(matrix);
        assertThat(result).hasSize(1);
        assertThat(result[0].rowSum()).isEqualTo(33);
        assertThat(result[0].colSum()).isEqualTo(33);
    }

    @Test
    void whenEmptyMatrixThenReturnEmptyArray() {
        int[][] matrix = new int[0][0];
        RolColSum.Sums[] result = RolColSum.sum(matrix);
        assertThat(result).isEmpty();
    }

    @Test
    void whenMatrixHasEmptyRowsThenReturnEmptyArray() {
        int[][] matrix = {
                {},
                {},
                {}
        };
        RolColSum.Sums[] result = RolColSum.sum(matrix);
        assertThat(result).isEmpty();
    }

    @Test
    void whenMatrixIsNotSquareThenThrowException() {
        int[][] matrix = {
                {1, 2, 3},
                {4, 5},
                {6}
        };
        assertThatThrownBy(() -> RolColSum.sum(matrix))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Matrix must be square and non-null");
    }

    @Test
    void whenMatrixContainsNullRowThenThrowException() {
        int[][] matrix = new int[3][];
        matrix[0] = new int[]{1, 2, 3};
        matrix[1] = null;
        matrix[2] = new int[]{7, 8, 9};
        assertThatThrownBy(() -> RolColSum.sum(matrix))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Matrix must be square and non-null");
    }

    @Test
    void whenNullMatrixThenThrowException() {
        int[][] matrix = null;
        assertThatThrownBy(() -> RolColSum.sum(matrix))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Matrix must be square and non-null");
    }
}