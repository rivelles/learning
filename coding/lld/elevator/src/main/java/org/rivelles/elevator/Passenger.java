package org.rivelles.elevator;

public class Passenger {
    private final String id;
    private int currentFloor;

    public Passenger(String id, int currentFloor) {
        this.id = id;
        this.currentFloor = currentFloor;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void arriveIn(int floor) {
        this.currentFloor = floor;
    }
}
