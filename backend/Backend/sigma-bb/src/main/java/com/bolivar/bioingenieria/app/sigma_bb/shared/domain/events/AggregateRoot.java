package com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events;

import java.util.ArrayList;
import java.util.List;

public class AggregateRoot {
    private final List<DomainEvent<? extends Payload>> events = new ArrayList<>();

    protected void registerEvent(DomainEvent<? extends Payload> event) {
        this.events.add(event);
    }

    public List<DomainEvent<? extends Payload>> pullEvents() {
        List<DomainEvent<? extends Payload>> result = new ArrayList<>(this.events);
        this.events.clear();
        return result;
    }
}
