package org.rivelles.parkinglot.vehicle;

import java.math.BigDecimal;
import java.util.Objects;

public class Car extends BaseVehicle {
    protected Car(String plate, boolean isHandicapped) {
        super(plate, isHandicapped);
    }

    @Override
    public VehicleType getType() {
        return VehicleType.CAR;
    }

    @Override
    public BigDecimal getBasePrice() {
        return BigDecimal.valueOf(6L);
    }
}
