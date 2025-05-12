package ru.job4j.threadlocal;

public class SecondThread extends Thread {
    @Override
    public void run() {
        ThreadLocalDemo.threadLocal.set("Это поток 2.");
        System.out.println(ThreadLocalDemo.threadLocal.get());
    }
}
