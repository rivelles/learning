package org.rivelles.elevator;

import org.rivelles.elevator.exceptions.ElevatorNotStoppedException;
import org.rivelles.elevator.exceptions.FullElevatorException;
import org.rivelles.elevator.exceptions.PassengerInDifferentFloorsException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArraySet;

import static org.rivelles.elevator.ElevatorDirection.*;
import static org.rivelles.elevator.ElevatorStatus.*;

public class Elevator {
    private final int maxPassengers;
    private CopyOnWriteArraySet<Passenger> passengers;
    private ElevatorStatus status;
    private ElevatorDirection direction;
    private int currentFloor;
    private ConcurrentSkipListSet<Integer> stops;

    public Elevator(int maxPassengers) {
        this.maxPassengers = maxPassengers;
        this.status = STOPPED;
        this.stops = new ConcurrentSkipListSet<>();
        this.passengers = new CopyOnWriteArraySet<>();
        this.direction = UNDEFINED;
        this.currentFloor = 0;
    }

    public boolean isNotInEmergency() {
        return !status.equals(EMERGENCY);
    }

    public int getDistanceFrom(int level) {
        return Math.abs(level - currentFloor);
    }

    public void addStop(int floor) {
        stops.add(floor);
    }

    public synchronized void board(Passenger passenger, int desiredFloor) {
        validateBoarding(passenger);

        passengers.add(passenger);
        addStop(desiredFloor);
    }

    public synchronized void disembark(Passenger passenger) {
        validateElevatorStopped();

        passenger.arriveIn(currentFloor);
        passengers.remove(passenger);
    }

    public void emergencyStop() {
        this.status = EMERGENCY;
    }

    public void stop() {
        this.status = STOPPED;
    }

    public synchronized Optional<Integer> move() {
        switch (direction) {
            case UP -> {
                var next = stops.higher(currentFloor);
                if (next != null) return Optional.of(next);

                next = stops.lower(currentFloor);
                if (next != null) {
                    return moveDown(next);
                }
                return stay();
            }
            case DOWN -> {
                var next = stops.lower(currentFloor);
                if (next != null) return Optional.of(next);

                next = stops.higher(currentFloor);
                if (next != null) {
                    return moveUp(next);
                }
                return stay();
            }
            case UNDEFINED -> {
                var nextUp = stops.higher(currentFloor);
                var nextDown = stops.lower(currentFloor);
                if (nextDown == null && nextUp == null) return Optional.empty();
                if (nextUp != null && nextDown == null) {
                    return moveUp(nextUp);
                }
                if (nextUp == null) {
                    return moveDown(nextDown);
                }
                if (nextDown < nextUp) {
                    return moveDown(nextDown);
                }
                return moveUp(nextUp);
            }
        }
        return Optional.empty();
    }

    private Optional<Integer> stay() {
        this.direction = UNDEFINED;
        this.status = STOPPED;
        return Optional.empty();
    }

    private Optional<Integer> moveDown(Integer next) {
        this.direction = DOWN;
        this.status = MOVING;
        return Optional.of(next);
    }

    private Optional<Integer> moveUp(Integer next) {
        this.direction = UP;
        this.status = MOVING;
        return Optional.of(next);
    }

    private boolean isStopped() {
        return status.equals(STOPPED);
    }

    private void validateBoarding(Passenger passenger) {
        if (passengers.size() == maxPassengers)
            throw new FullElevatorException("Elevator is full");
        if (this.currentFloor != passenger.getCurrentFloor())
            throw new PassengerInDifferentFloorsException("Passenger needs to be in the same floor");
        validateElevatorStopped();
    }

    private void validateElevatorStopped() {
        if (!this.isStopped())
            throw new ElevatorNotStoppedException("Elevator is not stopped.");
    }
}
