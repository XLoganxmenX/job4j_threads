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
    private int count;

    public SimpleBlockingQueue(int size) {
        this.maxSize = size;
    }

    public void offer(T value) {
        synchronized (this) {
            while (count == maxSize) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            queue.offer(value);
            count++;
            this.notify();
        }
    }

    public T poll() throws InterruptedException {
        synchronized (this) {
            while (queue.isEmpty()) {
                this.wait();
            }

            T returnItem = queue.poll();
            count--;
            this.notify();
            return returnItem;
        }
    }
}
