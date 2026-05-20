package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.errors;

public class BrandNotFoundException extends RuntimeException {
    public BrandNotFoundException(String id) {
        super("Brand with id " + id + " not found");
    }
}
