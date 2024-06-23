package org.rivelles.elevator.exceptions;

public class PassengerInDifferentFloorsException extends IllegalStateException {
    public PassengerInDifferentFloorsException(String message) {
        super(message);
    }
}
