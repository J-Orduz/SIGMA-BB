package com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.domain.country.events;

import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.DomainEvent;
import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.EventMetadata;

public record CountryDeletedEvent(EventMetadata metadata, CountryPayload payload) implements DomainEvent<CountryPayload> {}
