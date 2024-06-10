package ru.job4j;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.LinkedList;
import java.util.Queue;

@ThreadSafe
public class SimpleBlockingQueue<T> {

    @GuardedBy("this")
    private final Queue<T> queue = new LinkedList<>();
    private final int maxSize;

    public SimpleBlockingQueue(int size) {
        this.maxSize = size;
    }

    public synchronized void offer(T value) throws InterruptedException {
        while (queue.size() == maxSize) {
            this.wait();
        }

        queue.offer(value);
        this.notify();
    }

    public synchronized T poll() throws InterruptedException {
        while (queue.isEmpty()) {
            this.wait();
        }

        T returnItem = queue.poll();
        this.notify();
        return returnItem;
    }
}
