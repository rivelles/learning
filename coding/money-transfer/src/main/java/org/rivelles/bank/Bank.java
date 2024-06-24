package org.rivelles.bank;

import org.rivelles.bank.exceptions.NotEnoughFundsException;
import org.rivelles.bank.exceptions.SameAccountTransferException;

import java.math.BigDecimal;

import static org.rivelles.bank.AmountValidator.assertAmount;

public class Bank {
    public void transfer(Account from, Account to, BigDecimal value) {
        if (from == to)
            throw new SameAccountTransferException("Can't transfer to the same account");
        assertAmount(value);

        Account lock1;
        Account lock2;
        if (from.getNumber().compareTo(to.getNumber()) < 0) {
            lock1 = from;
            lock2 = to;
        }
        else {
            lock1 = to;
            lock2 = from;
        }

        synchronized (lock1) {
            synchronized (lock2) {
                if (from.getBalance().subtract(value).compareTo(BigDecimal.ZERO) < 0)
                    throw new NotEnoughFundsException("Not enough funds");

                from.debit(value);
                to.credit(value);
            }
        }
    }
}
