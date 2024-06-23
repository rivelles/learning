package org.rivelles.parkinglot.parkingspot;

import org.rivelles.parkinglot.exceptions.IncompatibleVehicleException;
import org.rivelles.parkinglot.vehicle.Vehicle;

import java.util.Optional;

public abstract class BaseParkingSpot implements ParkingSpot {
    private Vehicle vehicle;
    private final boolean handicappedExclusive;
    BaseParkingSpot(boolean handicappedExclusive) {
        this.handicappedExclusive = handicappedExclusive;
    }

    @Override
    public synchronized void park(Vehicle vehicle) {
        if (!this.canPark(vehicle)) {
            throw new IncompatibleVehicleException("Can't park vehicle %s on spot %s".formatted(vehicle.getPlate(), this.getSize()));
        }
        this.vehicle = vehicle;
    }

    @Override
    public Optional<Vehicle> getParkedVehicle() {
        return Optional.ofNullable(vehicle);
    }

    @Override
    public boolean isHandicappedExclusive() {
        return this.handicappedExclusive;
    }

    @Override
    public void release() {
        this.vehicle = null;
    }
}
