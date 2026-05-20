package com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.infrastructure.output.errors;

public class CountryNotFoundException extends RuntimeException {
    public CountryNotFoundException(String id) {
        super("Country with id " + id + " not found");
    }
}
