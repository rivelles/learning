package org.rivelles.parkinglot.ticket;

import org.rivelles.parkinglot.parkingspot.ParkingSpot;
import org.rivelles.parkinglot.vehicle.Vehicle;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.HOURS;
import static org.rivelles.parkinglot.ticket.TicketStatus.FINISHED;
import static org.rivelles.parkinglot.ticket.TicketStatus.PENDING;

public class Ticket {
    private final ParkingSpot parkingSpot;

    private final Instant startTime;

    private Instant endTime;
    public TicketStatus status;

    public Ticket(ParkingSpot parkingSpot) {
        this.parkingSpot = parkingSpot;
        this.startTime = Instant.now();
        this.status = PENDING;
    }

    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public Optional<Instant> getEndTime() {
        return Optional.ofNullable(endTime);
    }

    public boolean isPending() {
        return status.equals(PENDING);
    }

    public Optional<Vehicle> getParkedVehicle() {
        return parkingSpot.getParkedVehicle();
    }

    public void finish() {
        this.endTime = Instant.now();
        this.status = FINISHED;
    }

    public Duration getDuration() {
        if (endTime == null) return Duration.between(startTime, Instant.now());
        return Duration.between(startTime, endTime);
    }

    public BigDecimal getCost() {
        var durationInHours = BigDecimal.valueOf(getDuration().get(HOURS));

        return parkingSpot
            .getParkedVehicle()
            .map(Vehicle::getBasePrice)
            .map(basePrice -> basePrice.multiply(durationInHours))
            .orElse(BigDecimal.ZERO);
    }
}
