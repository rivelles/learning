package org.rivelles.elevator;

import java.util.List;
import java.util.Optional;

public class Building {
    public static final int MAX_FLOOR = 100;
    private final List<Elevator> elevators;

    public Building(List<Elevator> elevators) {
        this.elevators = elevators;
    }

    public Optional<Elevator> requestElevator(int currentFloor) {
        var availableElevators = elevators
            .stream()
            .filter(Elevator::isNotInEmergency)
            .toList();
        if (availableElevators.isEmpty()) return Optional.empty();

        var chosenElevator = findClosestElevator(currentFloor, availableElevators);

        return chosenElevator.map(elevator -> {
            elevator.addStop(currentFloor);
            return elevator;
        });
    }

    private static Optional<Elevator> findClosestElevator(int currentFloor, List<Elevator> availableElevators) {
        Optional<Elevator> chosenElevator = Optional.empty();
        int minDistance = Integer.MAX_VALUE;
        for (Elevator e : availableElevators) {
            int distance = e.getDistanceFrom(currentFloor);
            if (distance < minDistance) {
                chosenElevator = Optional.of(e);
                if (distance == 0) break;
            }
            minDistance = Math.min(minDistance, distance);
        }
        return chosenElevator;
    }
}
