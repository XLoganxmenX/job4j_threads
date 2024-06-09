package ru.job4j;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.LinkedList;
import java.util.Queue;

@ThreadSafe
public class SimpleBlockingQueue<T> {

    @GuardedBy("this")
    private final Queue<T> queue = new LinkedList<>();
    private int remainSize;

    public SimpleBlockingQueue(int size) {
        this.remainSize = size;
    }

    public synchronized void offer(T value) throws InterruptedException {
        while (remainSize == 0) {
            this.wait();
        }

        queue.offer(value);
        remainSize--;
        this.notify();
    }

    public synchronized T poll() throws InterruptedException {
        while (queue.isEmpty()) {
            this.wait();
        }

        T returnItem = queue.poll();
        remainSize++;
        this.notify();
        return returnItem;
    }
}
