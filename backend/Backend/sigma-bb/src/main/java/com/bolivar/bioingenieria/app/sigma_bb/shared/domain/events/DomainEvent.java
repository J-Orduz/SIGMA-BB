package com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events;

import java.io.Serializable;

public interface DomainEvent<T extends Payload> extends Serializable {
    EventMetadata metadata();
    T payload();
}
