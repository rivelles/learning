package org.rivelles.parkinglot.vehicle;

import java.util.Objects;

public abstract class BaseVehicle implements Vehicle {
    private final String plate;
    private final boolean isHandicapped;

    BaseVehicle(String plate, boolean isHandicapped) {
        this.plate = plate;
        this.isHandicapped = isHandicapped;
    }

    @Override
    public String getPlate() {
        return plate;
    }

    @Override
    public boolean isHandicapped() {
        return isHandicapped;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseVehicle that = (BaseVehicle) o;
        return Objects.equals(plate, that.plate);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(plate);
    }
}
