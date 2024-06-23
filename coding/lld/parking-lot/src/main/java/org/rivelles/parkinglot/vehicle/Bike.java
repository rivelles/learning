package org.rivelles.parkinglot.vehicle;

import java.math.BigDecimal;

public class Bike extends BaseVehicle {
    protected Bike(String plate, boolean isHandicapped) {
        super(plate, isHandicapped);
    }

    @Override
    public VehicleType getType() {
        return VehicleType.BIKE;
    }

    @Override
    public BigDecimal getBasePrice() {
        return BigDecimal.valueOf(3L);
    }
}
