package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.model.events;

import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.DomainEvent;
import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.EventMetadata;

public record ModelCreatedEvent(EventMetadata metadata, ModelPayload payload) implements DomainEvent<ModelPayload> {}
