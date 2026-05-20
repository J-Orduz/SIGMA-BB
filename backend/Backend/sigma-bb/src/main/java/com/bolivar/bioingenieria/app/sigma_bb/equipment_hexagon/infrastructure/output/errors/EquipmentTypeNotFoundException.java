package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.errors;

public class EquipmentTypeNotFoundException extends RuntimeException {
    public EquipmentTypeNotFoundException(String id) {
        super("EquipmentType with id " + id + " not found");
    }
}
