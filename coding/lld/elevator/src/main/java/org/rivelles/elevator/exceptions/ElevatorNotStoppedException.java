package org.rivelles.elevator.exceptions;

public class ElevatorNotStoppedException extends IllegalArgumentException {
    public ElevatorNotStoppedException(String message) {
        super(message);
    }
}
