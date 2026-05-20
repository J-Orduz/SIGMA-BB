package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.tecnical_verification_equipment.events;

import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.DomainEvent;
import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.EventMetadata;

public record TechnicalVerificationEquipmentDeleted
        (EventMetadata metadata,
        TechnicalVerificationEquipmentPayload payload)
        implements DomainEvent<TechnicalVerificationEquipmentPayload> {}
