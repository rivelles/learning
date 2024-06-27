package org.rivelles.bank;

import java.math.BigDecimal;
import java.util.concurrent.locks.StampedLock;

public class Account {
    private final StampedLock lock = new StampedLock();

    public String getNumber() {
        return number;
    }

    private final String number;

    private BigDecimal balance;

    public Account(String number) {
        this.number = number;
        this.balance = BigDecimal.ZERO;
    }

    public void credit(BigDecimal amount) {
        executeWithWriteLock(() -> balance = balance.add(amount));
    }

    public void debit(BigDecimal amount) {
        executeWithWriteLock(() -> balance = balance.subtract(amount));
    }

    public BigDecimal getBalance() {
        BigDecimal balance;
        var stamp = lock.tryOptimisticRead();
        balance = this.balance;
        if (!lock.validate(stamp)) {
            stamp = lock.readLock();
            try {
                return this.balance;
            }
            finally {
                lock.unlock(stamp);
            }
        }
        return balance;
    }

    private void executeWithWriteLock(Runnable runnable) {
        var stamp = lock.writeLock();
        try {
            runnable.run();
        } finally {
            lock.unlock(stamp);
        }
    }
}
