package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.tecnical_verification_equipment.events;

import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.Payload;

import java.util.UUID;

public record TechnicalVerificationEquipmentPayload
        (UUID verificationUUID, UUID equipmentTypeId) implements Payload {
}
