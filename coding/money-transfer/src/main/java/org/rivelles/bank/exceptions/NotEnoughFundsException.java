package org.rivelles.bank.exceptions;

public class NotEnoughFundsException extends RuntimeException {
    public NotEnoughFundsException(String s) {
        super(s);
    }
}
