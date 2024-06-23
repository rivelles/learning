package org.rivelles.parkinglot.parkingspot;

import org.rivelles.parkinglot.vehicle.Vehicle;
import org.rivelles.parkinglot.vehicle.VehicleType;

import static org.rivelles.parkinglot.parkingspot.HandicappedValidator.notHandicappedCompatible;

public class SmallParkingSpot extends BaseParkingSpot {
    SmallParkingSpot(boolean handicappedExclusive) {
        super(handicappedExclusive);
    }

    @Override
    public ParkingSpotSize getSize() {
        return ParkingSpotSize.SMALL;
    }

    @Override
    public boolean canPark(Vehicle vehicle) {
        if (!vehicle.getType().equals(VehicleType.BIKE)) return false;
        return !notHandicappedCompatible(this, vehicle);
    }
}
