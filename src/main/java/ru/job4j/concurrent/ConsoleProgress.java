package ru.job4j.concurrent;

public class ConsoleProgress implements Runnable {

    @Override
    public void run() {
        var process = new char[] {'-', '\\', '|', '/'};
        int index = 0;
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                break;
            }
            System.out.print("\r load: " + process[index]);
            index = (index + 1) % process.length;
        }
        System.out.print("\r load: Finished!");
    }

    public static void main(String[] args) {
        Thread progress = new Thread(new ConsoleProgress());
        progress.start();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        progress.interrupt();
    }
}
