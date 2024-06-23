package org.rivelles.parkinglot.exceptions;

public class ParkingLotAlreadyInitializedException extends RuntimeException {

    public ParkingLotAlreadyInitializedException(String message) {
        super(message);
    }
}
