package com.bolivar.bioingenieria.app.sigma_bb.event_persister_hexagon.infrastructure.output;

import com.bolivar.bioingenieria.app.sigma_bb.event_persister_hexagon.application.ports.output.EventStorePort;
import com.bolivar.bioingenieria.app.sigma_bb.event_persister_hexagon.domain.model.EventStoreEntry;
import com.bolivar.bioingenieria.app.sigma_bb.event_persister_hexagon.infrastructure.output.mapper.EventStorePersistenceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Component
public class EventStorePersistenceAdapter implements EventStorePort {
    private final EventStoreRepository repository;
    private final EventStorePersistenceMapper mapper;

    @Autowired
    public EventStorePersistenceAdapter(EventStoreRepository repository,
                                         EventStorePersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public void save(EventStoreEntry entry) {
        EventStoreEntity entity = mapper.toEntity(entry);
        entity.setCreatedAt(Instant.now());
        repository.save(entity);
    }
}
