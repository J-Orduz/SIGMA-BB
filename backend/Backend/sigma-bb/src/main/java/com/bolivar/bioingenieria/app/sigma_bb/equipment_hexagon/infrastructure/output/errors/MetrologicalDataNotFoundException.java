package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.errors;

public class MetrologicalDataNotFoundException extends RuntimeException {
    public MetrologicalDataNotFoundException(String id) {
        super("MetrologicalData with id " + id + " not found");
    }
}
