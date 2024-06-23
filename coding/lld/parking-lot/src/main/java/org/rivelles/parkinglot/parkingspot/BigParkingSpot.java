package org.rivelles.parkinglot.parkingspot;

import org.rivelles.parkinglot.vehicle.Vehicle;

import static org.rivelles.parkinglot.parkingspot.HandicappedValidator.notHandicappedCompatible;

public class BigParkingSpot extends BaseParkingSpot {
    BigParkingSpot(boolean handicappedExclusive) {
        super(handicappedExclusive);
    }

    @Override
    public ParkingSpotSize getSize() {
        return ParkingSpotSize.BIG;
    }

    @Override
    public boolean canPark(Vehicle vehicle) {
        return !notHandicappedCompatible(this, vehicle);
    }
}
