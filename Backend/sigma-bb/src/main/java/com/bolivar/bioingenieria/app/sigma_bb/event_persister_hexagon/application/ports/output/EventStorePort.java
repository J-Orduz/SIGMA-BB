package com.bolivar.bioingenieria.app.sigma_bb.event_persister_hexagon.application.ports.output;

import com.bolivar.bioingenieria.app.sigma_bb.event_persister_hexagon.domain.model.EventStoreEntry;

public interface EventStorePort {
    void save(EventStoreEntry entry);
}
