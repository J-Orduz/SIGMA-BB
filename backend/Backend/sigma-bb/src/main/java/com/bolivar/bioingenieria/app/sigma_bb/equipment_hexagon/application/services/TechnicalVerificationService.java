package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.ports.input.TechnicalVerificationServicePort;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.ports.output.TechnicalVerificationPersistencePort;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.technical_verification_services.commands.CreateTechnicalVerificationCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.technical_verification_services.commands.DeleteTechnicalVerificationCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.technical_verification_services.commands.UpdateTechnicalVerificationCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.technical_verification.TechnicalVerification;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.errors.TechnicalVerificationNotFoundException;
import com.bolivar.bioingenieria.app.sigma_bb.shared.application.ports.output.EventDispatcherPort;
import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.DomainEvent;
import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.Payload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TechnicalVerificationService implements TechnicalVerificationServicePort {
    private final TechnicalVerificationPersistencePort persistencePort;
    private final EventDispatcherPort eventDispatcherPort;

    @Autowired
    public TechnicalVerificationService(TechnicalVerificationPersistencePort persistencePort,
                                        @Qualifier(value = "springDispatcher") EventDispatcherPort eventDispatcherPort) {
        this.persistencePort = persistencePort;
        this.eventDispatcherPort = eventDispatcherPort;
    }

    @Override
    public List<TechnicalVerification> findAll() {
        return persistencePort.findAll();
    }

    @Override
    public TechnicalVerification findById(String id) {
        return this.persistencePort.findById(id)
                .orElseThrow(() -> new TechnicalVerificationNotFoundException(id));
    }

    @Override
    public TechnicalVerification save(CreateTechnicalVerificationCommand command) {
        TechnicalVerification tv = TechnicalVerification.create(command.description(), command.verificationType());
        persistencePort.save(tv);
        dispatchEvents(tv);
        return tv;
    }

    @Override
    public TechnicalVerification update(String id, UpdateTechnicalVerificationCommand command) {
        TechnicalVerification tv = persistencePort.findById(id)
                .orElseThrow(() -> new TechnicalVerificationNotFoundException(id));
        tv.updateTechnicalVerification(command.description(), command.verificationType());
        persistencePort.update(id, tv);
        dispatchEvents(tv);
        return tv;
    }

    @Override
    public void delete(DeleteTechnicalVerificationCommand command) {
        TechnicalVerification tv = persistencePort.findById(command.id())
                .orElseThrow(() -> new TechnicalVerificationNotFoundException(command.id()));
        tv.deleteTechnicalVerification();
        persistencePort.delete(command.id());
        dispatchEvents(tv);
    }

    private void dispatchEvents(TechnicalVerification aggregate) {
        List<DomainEvent<? extends Payload>> events = aggregate.pullEvents();
        events.forEach(eventDispatcherPort::dispatch);
    }
}
