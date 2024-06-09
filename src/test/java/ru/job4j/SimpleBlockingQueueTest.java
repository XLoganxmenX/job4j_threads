package ru.job4j;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class SimpleBlockingQueueTest {
    @Test
    public void whenOfferAndPoll() throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(10);
        List<Integer> testList = new ArrayList<>();
        Thread offerThread = new Thread(() -> {
            try {
                queue.offer(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread poolThread = new Thread(
                () -> {
                    try {
                        testList.add(queue.poll());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });

        offerThread.start();
        poolThread.start();
        offerThread.join();
        poolThread.join();

        assertThat(testList).containsExactly(1);
    }

    @Test
    public void whenPollAndOffer() throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(10);
        List<Integer> testList = new ArrayList<>();
        Thread offerThread = new Thread(() -> {
            try {
                queue.offer(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        Thread poolThread = new Thread(
                () -> {
                    try {
                        testList.add(queue.poll());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });

        poolThread.start();
        offerThread.start();
        poolThread.join();
        offerThread.join();

        assertThat(testList).containsExactly(1);
    }

    @Test
    public void whenOfferAndPollWithSizeLimit() throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(1);
        List<Integer> testList = new ArrayList<>();
        Thread offerThread = new Thread(() -> {
            try {
                queue.offer(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        Thread secondOfferThread = new Thread(() -> {
            try {
                queue.offer(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        Thread poolThread = new Thread(
                () -> {
                    try {
                        testList.add(queue.poll());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });

        offerThread.start();
        secondOfferThread.start();
        poolThread.start();
        offerThread.join();
        secondOfferThread.join();
        poolThread.join();

        assertThat(testList).containsExactly(1);
    }

}