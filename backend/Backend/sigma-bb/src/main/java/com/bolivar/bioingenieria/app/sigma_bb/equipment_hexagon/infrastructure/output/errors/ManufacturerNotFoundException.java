package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.errors;

public class ManufacturerNotFoundException extends RuntimeException {
    public ManufacturerNotFoundException(String id) {
        super("Manufacturer with id " + id + " not found");
    }
}
