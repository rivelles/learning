package org.rivelles.parkinglot.exceptions;

public class PendingTicketException extends RuntimeException {
    public PendingTicketException(String message) {
        super(message);
    }
}
