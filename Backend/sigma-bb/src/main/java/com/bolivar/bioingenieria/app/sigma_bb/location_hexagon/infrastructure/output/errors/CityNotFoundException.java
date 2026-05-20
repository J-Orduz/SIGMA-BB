package com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.infrastructure.output.errors;

public class CityNotFoundException extends RuntimeException {
    public CityNotFoundException(String id) {
        super("City with id " + id + " not found");
    }
}
