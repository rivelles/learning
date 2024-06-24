package org.rivelles.bank;

import org.rivelles.bank.exceptions.InvalidAmountException;
import org.rivelles.bank.exceptions.NullAmountException;

import java.math.BigDecimal;

public class AmountValidator {
    static void assertAmount(BigDecimal value) {
        if (value == null)
            throw new NullAmountException("Amount can't be null");
        if (value.compareTo(BigDecimal.ZERO) <= 0)
            throw new InvalidAmountException("Amount needs to be positive");
    }
}
