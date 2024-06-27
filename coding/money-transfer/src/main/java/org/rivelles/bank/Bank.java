package org.rivelles.bank;

import org.rivelles.bank.exceptions.InvalidTransactionException;
import org.rivelles.bank.exceptions.NotEnoughFundsException;

import java.math.BigDecimal;

public class Bank {
    public void transfer(Account from, Account to, BigDecimal value) {
        validateInputs(from, to, value);

        var lock1 = from.getNumber().compareTo(to.getNumber()) > 0 ? from : to;
        var lock2 = from.getNumber().compareTo(to.getNumber()) < 0 ? to : from;
        synchronized (lock1) {
            synchronized (lock2) {
                if (from.getBalance().subtract(value).compareTo(BigDecimal.ZERO) < 0)
                    throw new NotEnoughFundsException("Account %s has not enough funds");

                from.debit(value);
                to.credit(value);
            }
        }
    }

    public void transferNotThreadSafe(Account from, Account to, BigDecimal value) {
        validateInputs(from, to, value);

        if (from.getBalance().subtract(value).compareTo(BigDecimal.ZERO) < 0)
            throw new NotEnoughFundsException("Account %s has not enough funds");

        from.debit(value);
        to.credit(value);
    }

    private static void validateInputs(Account from, Account to, BigDecimal value) {
        if (from == null || to == null)
            throw new InvalidTransactionException("Accounts can't be null");
        if (value.compareTo(BigDecimal.ZERO) <= 0)
            throw new InvalidTransactionException("Value needs to be positive");
        if (from == to)
            throw new InvalidTransactionException("Can't transfer to the same account");
    }
}
