package com.bolivar.bioingenieria.app.sigma_bb.event_persister_hexagon.infrastructure.input.adapter;

import com.bolivar.bioingenieria.app.sigma_bb.event_persister_hexagon.application.ports.input.EventListenerPort;
import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.DomainEvent;
import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.Payload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EventListenerSpring {
    private final EventListenerPort port;

    @Autowired
    public EventListenerSpring(EventListenerPort port) {
        this.port = port;
    }

    @EventListener(condition = "#event.metadata().eventTopic() == 'events-domain'")
    public void listen(DomainEvent<? extends Payload> event) {
        System.out.println("Spring Event: " + event);
        port.handle(event);
    }
}
