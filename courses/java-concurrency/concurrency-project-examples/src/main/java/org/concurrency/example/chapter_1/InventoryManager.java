package org.concurrency.example.chapter_1;

public class InventoryManager implements Runnable {
    private int itemCount = 200000;
    public int getItemCount() {
        return itemCount;
    }

    @Override
    public void run() {
        String function = Thread.currentThread().getName();

        if(function.equals("issue")) {
            for(int i = 0; i < 100000; i++) {
                synchronized (this) {
                    itemCount--;
                }
            }
        } else if(function.equals("receive")) {
            for(int i = 0; i < 100000; i++) {
                synchronized (this) {
                    itemCount++;
                }
            }
        }
    }

    public static void main(String[] args) {
        InventoryManager inventoryManager = new InventoryManager();
        Thread issuer = new Thread(inventoryManager, "issue");
        Thread receiver = new Thread(inventoryManager, "receive");

        issuer.start();
        receiver.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Current number of items in inventory: " + inventoryManager.getItemCount());
    }
}