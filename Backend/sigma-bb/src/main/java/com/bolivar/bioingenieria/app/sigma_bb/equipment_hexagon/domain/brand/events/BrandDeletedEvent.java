package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.brand.events;

import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.DomainEvent;
import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.EventMetadata;

public record BrandDeletedEvent(EventMetadata metadata, BrandPayload payload) implements DomainEvent<BrandPayload> {}
