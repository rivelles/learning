package org.rivelles.parkinglot.parkingspot;

import org.rivelles.parkinglot.parkingspot.ParkingSpot;
import org.rivelles.parkinglot.vehicle.Vehicle;

public class HandicappedValidator {
    public static boolean notHandicappedCompatible(ParkingSpot parkingSpot, Vehicle vehicle) {
        return parkingSpot.isHandicappedExclusive() && !vehicle.isHandicapped();
    }
}
