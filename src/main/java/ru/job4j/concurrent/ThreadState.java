package ru.job4j.concurrent;

public class ThreadState {
    public static void main(String[] args) {
        Thread first = new Thread(
                () -> System.out.println(Thread.currentThread().getName() + " is running")
        );
        System.out.println(first.getState());
        Thread second = new Thread(
                () -> System.out.println(Thread.currentThread().getName() + " is running")
        );
        System.out.println(second.getState());
        first.start();
        second.start();
        while (first.getState() != Thread.State.TERMINATED || second.getState() != Thread.State.TERMINATED) {
            System.out.println(first.getName() + " is " + first.getState());
            System.out.println(second.getName() + " is " + second.getState());
        }
        System.out.println(first.getName() + " is " + first.getState());
        System.out.println(second.getName() + " is " + second.getState());
        System.out.println(Thread.currentThread().getName() + ": the work of the first and second threads is completed");
    }
}
