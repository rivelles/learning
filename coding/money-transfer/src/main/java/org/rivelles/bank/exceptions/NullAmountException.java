package org.rivelles.bank.exceptions;

public class NullAmountException extends IllegalArgumentException {
    public NullAmountException(String s) {
        super(s);
    }
}
