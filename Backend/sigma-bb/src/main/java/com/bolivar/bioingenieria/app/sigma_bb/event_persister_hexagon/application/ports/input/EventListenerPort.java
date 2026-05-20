package com.bolivar.bioingenieria.app.sigma_bb.event_persister_hexagon.application.ports.input;

import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.DomainEvent;
import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.Payload;

public interface EventListenerPort {
    void handle(DomainEvent<? extends Payload> event);
}
