package com.bolivar.bioingenieria.app.sigma_bb.event_persister_hexagon.infrastructure.output.mapper;

import com.bolivar.bioingenieria.app.sigma_bb.event_persister_hexagon.domain.model.EventStoreEntry;
import com.bolivar.bioingenieria.app.sigma_bb.event_persister_hexagon.infrastructure.output.EventStoreEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventStorePersistenceMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    EventStoreEntity toEntity(EventStoreEntry entry);

    EventStoreEntry toDomain(EventStoreEntity entity);
}
