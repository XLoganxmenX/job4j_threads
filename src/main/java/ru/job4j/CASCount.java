package ru.job4j;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicInteger;

@ThreadSafe
public class CASCount {
    private final AtomicInteger count = new AtomicInteger();

    public void increment() {
        int current;
        int nextValue;
        do {
            current = get();
            nextValue = current + 1;
        } while (!count.compareAndSet(current, nextValue));
    }

    public int get() {
        return count.get();
    }
}