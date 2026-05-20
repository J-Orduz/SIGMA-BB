package com.bolivar.bioingenieria.app.sigma_bb.event_persister_hexagon.application.services;

import com.bolivar.bioingenieria.app.sigma_bb.event_persister_hexagon.application.ports.input.EventListenerPort;
import com.bolivar.bioingenieria.app.sigma_bb.event_persister_hexagon.application.ports.output.EventStorePort;
import com.bolivar.bioingenieria.app.sigma_bb.event_persister_hexagon.domain.model.EventStoreEntry;
import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.DomainEvent;
import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.Payload;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventPersisterService implements EventListenerPort {

    private final EventStorePort eventStorePort;

    @Autowired
    public EventPersisterService(EventStorePort eventStorePort) {
        this.eventStorePort = eventStorePort;
    }

    @Override
    public void handle(DomainEvent<? extends Payload> event) {
        String metadataJson = safeToJson(event.metadata());
        String payloadJson = safeToJson(event.payload());

        EventStoreEntry entry = EventStoreEntry.builder()
                .eventId(event.metadata().eventId())
                .aggregateId(event.metadata().aggregateId())
                .aggregateType(event.metadata().aggregateType())
                .eventType(event.metadata().eventType())
                .version(event.metadata().version())
                .occurredAt(event.metadata().occurredAt())
                .metadataJson(metadataJson)
                .payloadJson(payloadJson)
                .build();

        eventStorePort.save(entry);
    }

    private String safeToJson(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            return "";
        }
    }
}
