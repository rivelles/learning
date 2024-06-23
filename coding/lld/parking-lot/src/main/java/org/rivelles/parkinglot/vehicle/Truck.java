package org.rivelles.parkinglot.vehicle;

import java.math.BigDecimal;

public class Truck extends BaseVehicle {

    static final BigDecimal TRUCK_BASE_PRICE = BigDecimal.valueOf(10L);

    protected Truck(String plate, boolean isHandicapped) {
        super(plate, isHandicapped);
    }

    @Override
    public VehicleType getType() {
        return VehicleType.TRUCK;
    }

    @Override
    public BigDecimal getBasePrice() {
        return TRUCK_BASE_PRICE;
    }
}
