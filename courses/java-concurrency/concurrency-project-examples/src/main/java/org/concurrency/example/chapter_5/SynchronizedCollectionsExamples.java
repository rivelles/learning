package org.concurrency.example.chapter_5;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SynchronizedCollectionsExamples {
    //static List<Integer> myList = new ArrayList<>();
    static List<Integer> myList = new CopyOnWriteArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        var thread1 = new Thread(SynchronizedCollectionsExamples::cleanList);
        var thread2 = new Thread(SynchronizedCollectionsExamples::cleanList);

        for (int i=0; i < 1_000; i++) {
            myList.add(i);
        }

        thread1.start();
        thread2.start();
        Thread.sleep(1000L);

        System.out.println("Finished. Size of myList: " + myList.size());
    }

    private static void cleanList() {
        for (Integer integer : myList) {
            myList.remove(integer);
        }
    }
}