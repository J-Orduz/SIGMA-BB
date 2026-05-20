package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.equipment.events;

import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.DomainEvent;
import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.EventMetadata;

public record EquipmentCreatedEvent(EventMetadata metadata, EquipmentPayload payload) implements DomainEvent<EquipmentPayload> {}
