package org.rivelles.bank.exceptions;

public class InvalidTransactionException extends IllegalArgumentException {
    public InvalidTransactionException(String s) {
        super(s);
    }
}
