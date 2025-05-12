package ru.job4j.threads.threadlocal;

public class ThreadLocalDemo {
    public static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static void main(String[] args) {
        Thread first = new FirstThread();
        Thread second = new SecondThread();
        threadLocal.set("Это поток main.");
        System.out.println(threadLocal.get());
        first.start();
        second.start();
        try {
            first.join();
            second.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
