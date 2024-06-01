package org.concurrency.example.chapter_1;

public class ConcurrentAccessRunnable implements Runnable {
    public int counter = 0;
    @Override
    public void run() {
        for (int i = 0; i < 1_000_000; i++) {
            counter++;
        }
    }
}

class Main {
    public static void main(String[] args) throws InterruptedException {
        var runnable = new ConcurrentAccessRunnable();
        var thread1 = new Thread(runnable, "Thread 1");
        var thread2 = new Thread(runnable, "Thread 2");

        thread1.start();
        thread2.start();

        Thread.sleep(2000);
        System.out.println("Counter: " + runnable.counter);
    }
}
