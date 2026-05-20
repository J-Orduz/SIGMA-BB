package com.bolivar.bioingenieria.app.sigma_bb.shared.infrastructure.output.spring;

import com.bolivar.bioingenieria.app.sigma_bb.shared.application.ports.output.EventDispatcherPort;
import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.DomainEvent;
import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.Payload;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class SpringDispatcher implements EventDispatcherPort {

    private final ApplicationEventPublisher publisher;

    public SpringDispatcher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void dispatch(DomainEvent<? extends Payload> event) {
        publisher.publishEvent(event);
    }
}
