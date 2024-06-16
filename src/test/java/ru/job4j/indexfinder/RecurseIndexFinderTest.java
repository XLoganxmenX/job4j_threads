package ru.job4j.indexfinder;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;

class RecurseIndexFinderTest {
    @Test
    public void whenFindIndexInIntArray() {
        Integer[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9};

        assertThat(RecurseIndexFinder.find(array, 5)).isEqualTo(4);
    }

    @Test
    public void whenFindIndexInThreadArray() {
        Thread[] array = new Thread[100];
        Arrays.fill(array, new Thread());
        Thread toFind = new Thread();
        array[4] = toFind;

        assertThat(RecurseIndexFinder.find(array, toFind)).isEqualTo(4);
    }

    @Test
    public void whenFindIndexInStringArray() {
        String[] array = new String[100];
        Arrays.fill(array, "String");
        String toFind = "String To Find";
        array[4] = toFind;

        assertThat(RecurseIndexFinder.find(array, toFind)).isEqualTo(4);
    }

    @Test
    public void whenFindIndexNotExistItemThenNegativeOne() {
        String[] array = new String[100];
        Arrays.fill(array, "String");
        String toFind = "String To Find";

        assertThat(RecurseIndexFinder.find(array, toFind)).isEqualTo(-1);
    }

    @Test
    public void whenIndexFirstAndLast() {
        String[] array = new String[100];
        Arrays.fill(array, "String");
        String firstToFind = "First";
        String lastToFind = "Last";
        array[0] = firstToFind;
        array[array.length - 1] = lastToFind;

        assertThat(RecurseIndexFinder.find(array, firstToFind)).isEqualTo(0);
        assertThat(RecurseIndexFinder.find(array, lastToFind)).isEqualTo(array.length - 1);
    }
}