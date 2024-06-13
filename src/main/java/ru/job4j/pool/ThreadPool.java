package ru.job4j.pool;

import ru.job4j.SimpleBlockingQueue;

import java.util.LinkedList;
import java.util.List;

public class ThreadPool {
    private final List<Thread> threads = new LinkedList<>();
    private final SimpleBlockingQueue<Runnable> tasks = new SimpleBlockingQueue<>(10);
    private final int size = Runtime.getRuntime().availableProcessors();

    public ThreadPool() {
        for (int i = 0; i < size; i++) {
            Thread thread = new Thread(
                    () -> {
                        while (!tasks.isEmpty() || !Thread.currentThread().isInterrupted()) {
                            try {
                                tasks.poll();
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                        }
            });
            thread.start();
            threads.add(thread);
        }
    }

    public void work(Runnable job) {
        threads.stream()
                .filter(thread -> thread.getState() == Thread.State.WAITING)
                .forEach(Thread::notifyAll);

        try {
            tasks.offer(job);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void shutdown() {
        threads.forEach(Thread::interrupt);
    }
}