package com.bolivar.bioingenieria.app.sigma_bb.event_persister_hexagon.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventStoreEntry {
    private String eventId;
    private String aggregateId;
    private String aggregateType;
    private String eventType;
    private int version;
    private Instant occurredAt;
    private String metadataJson;
    private String payloadJson;
}
