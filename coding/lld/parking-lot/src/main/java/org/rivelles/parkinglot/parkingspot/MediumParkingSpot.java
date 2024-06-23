package org.rivelles.parkinglot.parkingspot;

import org.rivelles.parkinglot.vehicle.Vehicle;
import org.rivelles.parkinglot.vehicle.VehicleType;

import static org.rivelles.parkinglot.parkingspot.HandicappedValidator.notHandicappedCompatible;

public class MediumParkingSpot extends BaseParkingSpot {
    MediumParkingSpot(boolean handicappedExclusive) {
        super(handicappedExclusive);
    }

    @Override
    public ParkingSpotSize getSize() {
        return ParkingSpotSize.MEDIUM;
    }

    @Override
    public boolean canPark(Vehicle vehicle) {
        if (vehicle.getType().equals(VehicleType.TRUCK)) return false;
        return !notHandicappedCompatible(this, vehicle);
    }
}
