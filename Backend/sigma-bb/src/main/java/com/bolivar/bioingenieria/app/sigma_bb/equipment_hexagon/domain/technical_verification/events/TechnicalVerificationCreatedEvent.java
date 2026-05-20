package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.technical_verification.events;

import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.DomainEvent;
import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.EventMetadata;

public record TechnicalVerificationCreatedEvent(EventMetadata metadata, TechnicalVerificationPayload payload) implements DomainEvent<TechnicalVerificationPayload> {}
