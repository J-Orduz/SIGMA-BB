package com.bolivar.bioingenieria.app.sigma_bb.event_persister_hexagon.infrastructure.output;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventStoreRepository extends JpaRepository<EventStoreEntity, Long> {
}
