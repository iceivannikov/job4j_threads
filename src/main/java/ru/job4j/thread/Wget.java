package ru.job4j.thread;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;

public class Wget implements Runnable {
    private static final double NANOS_IN_MILLISECOND = 1_000_000.0;
    private final String url;
    private final int speed;

    public Wget(String url, int speed) {
        this.url = url;
        this.speed = speed;
    }

    @Override
    public void run() {
        var file = new File("tmp.xml");
        try (var input = new URL(url).openStream();
             var output = new FileOutputStream(file)) {
            var dataBuffer = new byte[512];
            while (true) {
                var startDownload = System.nanoTime();
                int bytesRead = input.read(dataBuffer, 0, dataBuffer.length);
                var downloadNanos = System.nanoTime() - startDownload;
                if (bytesRead == -1) {
                    break;
                }
                output.write(dataBuffer, 0, bytesRead);
                var downloadMs = downloadNanos / NANOS_IN_MILLISECOND;
                var expectedTime = bytesRead / speed;
                var sleepTime = Math.round(expectedTime - downloadMs);
                if (sleepTime > 0) {
                    Thread.sleep(sleepTime);
                }
                System.out.printf("Read %d bytes in %.2f ms, sleep for %d ms%n",
                        bytesRead, downloadMs, sleepTime);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        try {
            System.out.println(Files.size(file.toPath()) + " bytes");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void checkArguments(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException("Wrong number of arguments");
        }
        if (args[0] == null || args[1] == null) {
            throw new IllegalArgumentException("Wrong arguments");
        }
        if (!args[0].startsWith("http://") && !args[0].startsWith("https://")) {
            throw new IllegalArgumentException("URL must start with http:// or https://");
        }
        try {
            new URL(args[0]);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid URL format");
        }
        int speed;
        try {
            speed = Integer.parseInt(args[1]);
            if (speed <= 0) {
                throw new IllegalArgumentException("Speed must be a positive number");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Speed must be a number");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        checkArguments(args);
        String url = args[0];
        int speed = Integer.parseInt(args[1]);
        Thread wget = new Thread(new Wget(url, speed));
        wget.start();
        wget.join();
    }
}
