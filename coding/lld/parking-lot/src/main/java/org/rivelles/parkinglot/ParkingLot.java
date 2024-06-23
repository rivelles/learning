package org.rivelles.parkinglot;

import org.rivelles.parkinglot.exceptions.ParkingLotAlreadyInitializedException;
import org.rivelles.parkinglot.exceptions.PendingTicketException;
import org.rivelles.parkinglot.exceptions.VehicleAlreadyParkedException;
import org.rivelles.parkinglot.exceptions.VehicleNotFoundException;
import org.rivelles.parkinglot.parkingfloor.ParkingFloor;
import org.rivelles.parkinglot.ticket.Ticket;
import org.rivelles.parkinglot.vehicle.Vehicle;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Stream;

public class ParkingLot {
    private final String name;
    private final List<ParkingFloor> floors;
    private final List<Ticket> tickets;
    private static ParkingLot instance;

    private ParkingLot(String name, List<ParkingFloor> floors) {
        this.name = name;
        this.floors = floors;
        this.tickets = new ArrayList<>();
    }

    public static Optional<ParkingLot> getInstance() {
        return Optional.ofNullable(instance);
    }
    public static void initialize(String name, List<ParkingFloor> parkingFloors) {
        if (instance != null)
            throw new ParkingLotAlreadyInitializedException("Parking lot is already initialized.");

        instance = new ParkingLot(name, parkingFloors);
    }

    public boolean admitVehicle(Vehicle vehicle) {
        validateVehicleAlreadyParked(vehicle);

        return floors.stream()
            .map(ParkingFloor::getParkingSpots)
            .flatMap(Collection::stream)
            .filter(spot -> spot.canPark(vehicle))
            .findAny().map(parkingSpot -> {
                parkingSpot.park(vehicle);
                tickets.add(new Ticket(parkingSpot));

                return true;
            })
            .orElse(false);
    }

    public void release(Vehicle vehicle) {
        var vehicleTicket = getTicketForVehicleStream(vehicle)
            .findFirst()
            .orElseThrow(() -> new VehicleNotFoundException("Vehicle %s not found".formatted(vehicle.getPlate())));

        if (vehicleTicket.isPending()) {
            throw new PendingTicketException("Ticket is still pending");
        }

        vehicleTicket.getParkingSpot().release();
        vehicleTicket.finish();
    }

    public Optional<Duration> getDurationOfParking(Vehicle vehicle) {
        return getTicketForVehicleStream(vehicle)
            .map(Ticket::getDuration)
            .findFirst();
    }

    private Stream<Ticket> getTicketForVehicleStream(Vehicle vehicle) {
        return tickets.stream()
            .filter(ticket -> ticket.getParkedVehicle().isPresent() && ticket.getParkedVehicle().get().equals(vehicle));
    }

    private void validateVehicleAlreadyParked(Vehicle vehicle) {
        var pendingTicketForVehicle = tickets
            .stream()
            .filter(ticket -> ticket.isPending() && !ticket.getParkedVehicle().map(parkedVehicle -> parkedVehicle.equals(vehicle)).orElse(false))
            .findAny();

        if (pendingTicketForVehicle.isPresent()) {
            throw new VehicleAlreadyParkedException("Vehicle with plate %s is already parked".formatted(vehicle.getPlate()));
        }
    }
}
