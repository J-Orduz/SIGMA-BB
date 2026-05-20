package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.technical_verification.events;

import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.Payload;

public record TechnicalVerificationPayload(String description, String verificationType) implements Payload {}
