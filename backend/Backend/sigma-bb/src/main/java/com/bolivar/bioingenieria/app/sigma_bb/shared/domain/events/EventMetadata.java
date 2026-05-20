package com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events;

import java.io.Serializable;
import java.time.Instant;

public record EventMetadata(
        String eventTopic,
        String eventId,
        String aggregateType,
        String eventType,
        int version,
        Instant occurredAt,
        String aggregateId
) implements Serializable {}
