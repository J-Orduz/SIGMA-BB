package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.errors;

public class EquipmentNotFoundException extends RuntimeException {
    public EquipmentNotFoundException(String id) {
        super("Equipment with id " + id + " not found");
    }
}
