package org.rivelles.parkinglot.vehicle;

import java.math.BigDecimal;

public interface Vehicle {
    VehicleType getType();
    BigDecimal getBasePrice();
    String getPlate();
    boolean isHandicapped();
}
