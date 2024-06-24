package org.rivelles.bank.exceptions;

public class SameAccountTransferException extends IllegalArgumentException {
    public SameAccountTransferException(String s) {
        super(s);
    }
}
