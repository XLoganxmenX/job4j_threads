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
            long startTime = System.currentTimeMillis();
            long bytesDownloaded = 0;
            while ((bytesRead = input.read(dataBuffer, 0, dataBuffer.length)) != -1) {
                bytesDownloaded += bytesRead;

                if (bytesDownloaded >= speed) {
                    long endTime = System.currentTimeMillis();
                    long downloadTime = endTime - startTime;

                    if (speed != NO_PAUSE_THRESHOLD && downloadTime < 1000) {
                        Thread.sleep(bytesDownloaded);
                    }

                    bytesDownloaded = 0;
                    startTime = System.currentTimeMillis();
                }
                output.write(dataBuffer, 0, bytesRead);
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