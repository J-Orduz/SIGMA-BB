package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.errors;

public class TechnicalVerificationNotFoundException extends RuntimeException {
    public TechnicalVerificationNotFoundException(String id) {
        super("TechnicalVerification with id " + id + " not found");
    }
}
