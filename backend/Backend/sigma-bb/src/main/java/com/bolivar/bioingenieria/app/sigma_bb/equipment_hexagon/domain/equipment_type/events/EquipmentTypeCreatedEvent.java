package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.equipment_type.events;

import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.DomainEvent;
import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.EventMetadata;

public record EquipmentTypeCreatedEvent(EventMetadata metadata, EquipmentTypePayload payload) implements DomainEvent<EquipmentTypePayload> {}
