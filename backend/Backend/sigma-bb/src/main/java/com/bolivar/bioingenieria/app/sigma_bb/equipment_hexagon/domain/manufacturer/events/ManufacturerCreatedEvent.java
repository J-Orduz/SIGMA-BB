package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.manufacturer.events;

import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.DomainEvent;
import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.EventMetadata;

public record ManufacturerCreatedEvent(EventMetadata metadata, ManufacturerPayload payload) implements DomainEvent<ManufacturerPayload> {}
