package ru.job4j.email;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class EmailNotification {
    private final ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public void send(String subject, String body, String email) {
        throw new UnsupportedOperationException("Method send() is not implemented yet.");
    }

    public void emailTo(User user) {
        pool.submit(() -> {
            String userName = user.userName();
            String userEmail = user.email();
            Email email = new Email(
                    String.format("Notification {%s} to email {%s}", userName, userEmail),
                    String.format("Add a new event to {%s}", userName));
            send(email.subject(), email.body(), userEmail);
        });
    }

    public void close() {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
                pool.shutdownNow();
            }
        } catch (InterruptedException e) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
