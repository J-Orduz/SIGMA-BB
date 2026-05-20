package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.metrological_data.events;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.metrological_data.MetrologicalData;
import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.DomainEvent;
import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.EventMetadata;

public record MetrologicalDataCreatedEvent(EventMetadata metadata, MetrologicalData payload) implements DomainEvent<MetrologicalData> {}
