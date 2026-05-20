package com.bolivar.bioingenieria.app.sigma_bb.shared.application.ports.output;

import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.DomainEvent;
import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.Payload;

public interface EventDispatcherPort {
    void dispatch(DomainEvent<? extends Payload> event);
}
