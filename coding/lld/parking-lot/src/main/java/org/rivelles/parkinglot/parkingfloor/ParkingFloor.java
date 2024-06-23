package org.rivelles.parkinglot.parkingfloor;

import org.rivelles.parkinglot.parkingspot.ParkingSpot;

import java.util.Collections;
import java.util.List;

public class ParkingFloor {
    private final int level;
    private final List<ParkingSpot> parkingSpots;

    public ParkingFloor(int level, List<ParkingSpot> parkingSpots) {
        this.level = level;
        this.parkingSpots = Collections.unmodifiableList(parkingSpots);
    }

    public List<ParkingSpot> getParkingSpots() {
        return parkingSpots;
    }
}
