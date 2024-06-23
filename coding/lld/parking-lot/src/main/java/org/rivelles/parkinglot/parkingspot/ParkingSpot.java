package org.rivelles.parkinglot.parkingspot;

import org.rivelles.parkinglot.vehicle.Vehicle;

import java.util.Optional;

public interface ParkingSpot {
    ParkingSpotSize getSize();
    boolean canPark(Vehicle vehicle);

    void park(Vehicle vehicle);
    void release();

    Optional<Vehicle> getParkedVehicle();

    boolean isHandicappedExclusive();
}
