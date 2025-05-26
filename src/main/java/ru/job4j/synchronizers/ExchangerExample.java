package ru.job4j.synchronizers;

import java.util.concurrent.Exchanger;

public class ExchangerExample {
    public static void main(String[] args) {
        Exchanger<String> exchanger = new Exchanger<>();
        Thread producer = new Thread(() -> {
            try {
                String data = "Данные от производителя";
                System.out.println("Producer: подготовил данные");
                String response = exchanger.exchange(data);
                System.out.println("Producer: получил ответ — " + response);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        Thread consumer = new Thread(() -> {
            try {
                System.out.println("Consumer: ждёт данные");
                String received = exchanger.exchange("OK от потребителя");
                System.out.println("Consumer: получил — " + received);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        producer.start();
        consumer.start();
    }
}
