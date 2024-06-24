package org.rivelles.bank;

import org.rivelles.bank.exceptions.InvalidAmountException;
import org.rivelles.bank.exceptions.NullAmountException;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.locks.StampedLock;

import static org.rivelles.bank.AmountValidator.assertAmount;

public class Account {

    private final String number;

    private BigDecimal balance;

    public Account(String number) {
        this.number = number;
        this.balance = BigDecimal.ZERO;
    }


    public void credit(BigDecimal value) {
        assertAmount(value);

        synchronized (this) {
            balance = balance.add(value);
        }
    }

    public void debit(BigDecimal value) {
        assertAmount(value);

        synchronized (this) {
            balance = balance.subtract(value);
        }
    }

    public synchronized BigDecimal getBalance() {
        return balance;
    }

    public String getNumber() {
        return number;
    }
}
