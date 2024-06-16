package ru.job4j.indexfinder;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class RecurseIndexFinder<T> extends RecursiveTask<Integer> {
    private final T[] array;
    private final T item;
    private final int from;
    private final int to;

    public RecurseIndexFinder(T[] array, T item, int from, int to) {
        this.array = array;
        this.item = item;
        this.from = from;
        this.to = to;
    }

    @Override
    protected Integer compute() {
        if ((to - from) <= 10) {
            for (int i = from; i < to; i++) {
                if (array[i].equals(item)) {
                    return i;
                }
            }
            return -1;
        }
        int mid = (from + to) / 2;
        RecurseIndexFinder<T> leftFinder = new RecurseIndexFinder<>(array, item, from, mid);
        RecurseIndexFinder<T> rightFinder = new RecurseIndexFinder<>(array, item, mid, to);
        leftFinder.fork();
        rightFinder.fork();
        Integer leftResult = leftFinder.join();
        Integer rightResult = rightFinder.join();

        return (leftResult != -1) ? leftResult : rightResult;
    }

    public static <T> int find(T[] arr, T itemToFind) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        return forkJoinPool.invoke(new RecurseIndexFinder<T>(arr, itemToFind, 0, arr.length));
    }
}
