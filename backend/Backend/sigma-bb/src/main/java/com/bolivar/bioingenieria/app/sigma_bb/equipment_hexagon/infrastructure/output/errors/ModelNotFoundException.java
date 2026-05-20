package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.errors;

public class ModelNotFoundException extends RuntimeException {
    public ModelNotFoundException(String id) {
        super("Model with id " + id + " not found");
    }
}
