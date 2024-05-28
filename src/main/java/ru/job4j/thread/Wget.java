package ru.job4j.thread;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class Wget implements Runnable {
    private final String url;
    private final int speed;
    private static final int NO_PAUSE_THRESHOLD = 6000;
    private static final int BUFFER_SIZE = 1024;

    public Wget(String url, int speed) {
        this.url = url;
        this.speed = speed;
    }

    public static String urlValidate(String url) {
        try {
            new URL(url).toURI();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new IllegalArgumentException("URL is not correct");
        }

        return url;
    }

    public static int speedValidate(String arg) {
        int speed = Integer.parseInt(arg);
        if (speed < 0) {
            throw new IllegalArgumentException("Speed must be greater than 0");
        }

        return speed;
    }

    @Override
    public void run() {
        File file = new File(getFileNameFromURL(url));
        try (var input = new URL(url).openStream();
             var output = new FileOutputStream(file)) {
            byte[] dataBuffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = input.read(dataBuffer, 0, dataBuffer.length)) != -1) {

                long downloadAt = System.nanoTime();
                output.write(dataBuffer, 0, bytesRead);
                long readTime = (System.nanoTime() - downloadAt);

                double readSpeed = Math.round(((double) BUFFER_SIZE / readTime) * 1000000);
                if (speed != NO_PAUSE_THRESHOLD && readSpeed > speed) {
                    int pause = (int) readSpeed / speed;
                    Thread.sleep(pause);
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String getFileNameFromURL(String url) {
        String[] parts = url.split("/");
        return parts[parts.length - 1];
    }

    public static void main(String[] args) throws InterruptedException {
        String url = urlValidate(args[0]);
        int speed = speedValidate(args[1]);
        Thread wget = new Thread(new Wget(url, speed));
        wget.start();
        wget.join();
    }
}