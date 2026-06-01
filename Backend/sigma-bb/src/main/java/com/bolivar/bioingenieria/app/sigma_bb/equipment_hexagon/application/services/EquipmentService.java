package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.ports.input.EquipmentCommandServicePort;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.ports.output.EquipmentPersistencePort;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.equipment_services.commands.CreateEquipmentCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.equipment_services.commands.DeleteEquipmentCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.equipment_services.commands.UpdateEquipmentCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.equipment_services.commands.EquipmentPatchCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.equipment.Equipment;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.errors.EquipmentNotFoundException;
import com.bolivar.bioingenieria.app.sigma_bb.shared.application.ports.output.EventDispatcherPort;
import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.DomainEvent;
import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.Payload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentService implements EquipmentCommandServicePort {
    private final EquipmentPersistencePort persistencePort;
    private final EventDispatcherPort eventDispatcherPort;

    @Autowired
    public EquipmentService(EquipmentPersistencePort persistencePort,
                            @Qualifier(value = "springDispatcher") EventDispatcherPort eventDispatcherPort) {
        this.persistencePort = persistencePort;
        this.eventDispatcherPort = eventDispatcherPort;
    }

    @Override
    public Equipment save(CreateEquipmentCommand command) {
        Equipment equipment = Equipment.create(command.equipmentTypeId(), command.brandId());
        equipment = persistencePort.save(equipment);
        dispatchEvents(equipment);
        return equipment;
    }

    @Override
    public Equipment update(String id, UpdateEquipmentCommand command) {
        Equipment equipment = persistencePort.findById(id)
                .orElseThrow(() -> new EquipmentNotFoundException(id));
        equipment.updateEquipment(command.equipmentTypeId(), command.brandId());
        equipment = persistencePort.update(id, equipment);
        dispatchEvents(equipment);
        return equipment;
    }

    @Override
    public void delete(DeleteEquipmentCommand command) {
        Equipment equipment = persistencePort.findById(command.id())
                .orElseThrow(() -> new EquipmentNotFoundException(command.id()));
        equipment.deleteEquipment();
        persistencePort.delete(command.id());
        dispatchEvents(equipment);
    }

    @Override
    public Equipment patchUpdate(String id, EquipmentPatchCommand command) {
        Equipment equipment = persistencePort.findById(id)
                .orElseThrow(() -> new EquipmentNotFoundException(id));
        equipment.updateEquipmentPatch(command.equipmentTypeId(), command.brandId());
        equipment = persistencePort.update(id, equipment);
        dispatchEvents(equipment);
        return equipment;
    }

    private void dispatchEvents(Equipment aggregate) {
        List<DomainEvent<? extends Payload>> events = aggregate.pullEvents();
        events.forEach(eventDispatcherPort::dispatch);
    }
}
