package com.bolivar.bioingenieria.app.sigma_bb.shared.infrastructure.output.rabbit;

import com.bolivar.bioingenieria.app.sigma_bb.shared.application.ports.output.EventDispatcherPort;
import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.DomainEvent;
import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.Payload;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQDispatcher implements EventDispatcherPort {

    private static final String EXCHANGE = "events-domain";


    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitMQDispatcher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void dispatch(DomainEvent<? extends Payload> event) {
        rabbitTemplate.convertAndSend(EXCHANGE, "events-domains." + event.metadata().eventType(), event);
    }
}
