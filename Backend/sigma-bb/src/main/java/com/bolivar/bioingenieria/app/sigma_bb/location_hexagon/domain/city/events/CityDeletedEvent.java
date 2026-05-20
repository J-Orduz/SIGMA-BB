package com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.domain.city.events;

import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.DomainEvent;
import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.EventMetadata;

public record CityDeletedEvent(EventMetadata metadata, CityPayload payload) implements DomainEvent<CityPayload> {}
