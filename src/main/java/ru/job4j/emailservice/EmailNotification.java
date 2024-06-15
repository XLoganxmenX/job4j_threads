package ru.job4j.emailservice;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmailNotification {
    private final ExecutorService pool;

    public EmailNotification() {
        this.pool = Executors.newCachedThreadPool();
    }

    public void emailTo(User user) {
        String username = user.username();
        String email = user.email();
        String subject = String.format("Notification %s to email %s", username, email);
        String body = String.format("Add a new event to %s", username);

        pool.submit(() -> send(subject, body, email));
    }

    public void send(String subject, String body, String email) { }

    public void close() {
        pool.shutdown();
        while (!pool.isTerminated()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
