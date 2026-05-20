package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.ports.input.ManufacturerServicePort;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.ports.output.ManufacturerPersistencePort;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.manufacturer_services.commands.CreateManufacturerCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.manufacturer_services.commands.DeleteManufacturerCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.manufacturer_services.commands.UpdateManufacturerCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.manufacturer.Manufacturer;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.errors.ManufacturerNotFoundException;
import com.bolivar.bioingenieria.app.sigma_bb.shared.application.ports.output.EventDispatcherPort;
import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.DomainEvent;
import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.Payload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManufacterService implements ManufacturerServicePort {
    private final ManufacturerPersistencePort manufacterPersistencePort;
    private final EventDispatcherPort eventDispatcherPort;

    @Autowired
    public ManufacterService(ManufacturerPersistencePort manufacterPersistencePort,
                             @Qualifier(value = "springDispatcher") EventDispatcherPort eventDispatcherPort) {
        this.manufacterPersistencePort = manufacterPersistencePort;
        this.eventDispatcherPort = eventDispatcherPort;
    }

    @Override
    public List<Manufacturer> findAll() {
        return manufacterPersistencePort.findAll();
    }

    @Override
    public Manufacturer findById(String id) {
        return this.manufacterPersistencePort.findById(id)
                .orElseThrow(() -> new ManufacturerNotFoundException(id));
    }

    @Override
    public Manufacturer save(CreateManufacturerCommand command) {
        Manufacturer manufacturer = Manufacturer.create(command.name(), command.countryId());
        manufacterPersistencePort.save(manufacturer);
        dispatchEvents(manufacturer);
        return manufacturer;
    }

    @Override
    public Manufacturer update(String id, UpdateManufacturerCommand command) {
        Manufacturer manufacturer = manufacterPersistencePort.findById(id)
                .orElseThrow(() -> new ManufacturerNotFoundException(id));
        manufacturer.updateManufacturer(command.name(), command.countryId());
        manufacterPersistencePort.update(id, manufacturer);
        dispatchEvents(manufacturer);
        return manufacturer;
    }

    @Override
    public void delete(DeleteManufacturerCommand command) {
        Manufacturer manufacturer = manufacterPersistencePort.findById(command.id())
                .orElseThrow(() -> new ManufacturerNotFoundException(command.id()));
        manufacturer.deleteManufacturer();
        manufacterPersistencePort.delete(command.id());
        dispatchEvents(manufacturer);
    }

    private void dispatchEvents(Manufacturer aggregate) {
        List<DomainEvent<? extends Payload>> events = aggregate.pullEvents();
        events.forEach(eventDispatcherPort::dispatch);
    }
}
