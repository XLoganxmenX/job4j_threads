package ru.job4j;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CASCountTest {
    @Test
    public void whenIncrementInTwoThreadsThenNotBlocking() throws InterruptedException {
        CASCount counter = new CASCount();
        Thread first = new Thread(counter::increment);
        Thread second = new Thread(counter::increment);

        first.start();
        first.join();
        second.start();
        second.join();

        assertThat(counter.get()).isEqualTo(2);
    }

    @Test
    public void whenIncrementInMultiThreadsThenNotBlocking() throws InterruptedException {
        CASCount counter = new CASCount();

        for (int i = 0; i < 100; i++) {
            Thread thread = new Thread(counter::increment);
            thread.start();
            thread.join();
        }

        assertThat(counter.get()).isEqualTo(100);
    }

    @Test
    public void whenEachThreadIncrementMoreThanOneTimes() throws InterruptedException {
        CASCount counter = new CASCount();
        Thread first = new Thread(
                () -> {
                    counter.increment();
                    counter.increment();
                    counter.increment();
                    counter.increment();
                }
        );
        Thread second = new Thread(
                () -> {
                    counter.increment();
                    counter.increment();
                }
        );

        first.start();
        first.join();
        second.start();
        second.join();

        assertThat(counter.get()).isEqualTo(6);
    }
}