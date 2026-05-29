package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.ports.input.EquipmentTypeServicePort;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.ports.output.EquipmentTypePersistencePort;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.equipment_type_services.commands.EquipmentTypePatchCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.equipment_type_services.commands.CreateEquipmentTypeCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.equipment_type_services.commands.DeleteEquipmentTypeCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.equipment_type_services.commands.UpdateEquipmentTypeCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.metrological_data_services.commands.MetrologicalDataCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.equipment_type.EquipmentType;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.metrological_data.MetrologicalData;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.errors.EquipmentTypeNotFoundException;
import com.bolivar.bioingenieria.app.sigma_bb.shared.application.ports.output.EventDispatcherPort;
import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.DomainEvent;
import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.Payload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;


@Service
public class EquipmentTypeService implements EquipmentTypeServicePort {
    private final EquipmentTypePersistencePort equipmentTypePersistencePort;
    private final EventDispatcherPort eventDispatcherPort;

    @Autowired
    public EquipmentTypeService(EquipmentTypePersistencePort equipmentTypePersistencePort,
                                @Qualifier(value = "springDispatcher") EventDispatcherPort eventDispatcherPort) {
        this.equipmentTypePersistencePort = equipmentTypePersistencePort;
        this.eventDispatcherPort = eventDispatcherPort;
    }

    @Override
    public List<EquipmentType> findAll() {
        return equipmentTypePersistencePort.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public EquipmentType findById(String id) {
        return this.equipmentTypePersistencePort.findById(id)
                .orElseThrow(() -> new EquipmentTypeNotFoundException(id));
    }

    @Override
    public EquipmentType save(CreateEquipmentTypeCommand command) {
        EquipmentType et = EquipmentType.create(
                command.equipmentTypeName(),
                command.technicalDefinition(),
                command.careRecommendations(),
                command.voltage(),
                command.amperage(),
                command.predominantTechnology(),
                command.verifiable(),
                command.unitMaintenanceValue(),
                command.metrologicalData());

        equipmentTypePersistencePort.save(et);
        dispatchEvents(et);
        return et;
    }

    @Override
    public EquipmentType update(String id, UpdateEquipmentTypeCommand command) {
        EquipmentType et = equipmentTypePersistencePort.findById(id)
                .orElseThrow(() -> new EquipmentTypeNotFoundException(id));
        et.updateEquipmentType(
                command.equipmentTypeName(), command.technicalDefinition(),
                command.careRecommendations(), command.voltage(), command.amperage(),
                command.predominantTechnology(), command.verifiable(), command.unitMaintenanceValue());
        equipmentTypePersistencePort.update(id, et);
        dispatchEvents(et);
        return et;
    }

    @Override
    public void delete(DeleteEquipmentTypeCommand command) {
        EquipmentType et = equipmentTypePersistencePort.findById(command.id())
                .orElseThrow(() -> new EquipmentTypeNotFoundException(command.id()));
        et.deleteEquipmentType();
        equipmentTypePersistencePort.delete(command.id());
        dispatchEvents(et);
    }

    @Override
    public EquipmentType patchUpdate(String id, EquipmentTypePatchCommand command) {
        EquipmentType et = equipmentTypePersistencePort.findById(id)
                .orElseThrow(() -> new EquipmentTypeNotFoundException(id));
        et.updateEquipmentTypePatch(
                command.equipmentTypeName(), command.technicalDefinition(),
                command.careRecommendations(), command.voltage(), command.amperage(),
                command.predominantTechnology(), command.verifiable(), command.unitMaintenanceValue());
        equipmentTypePersistencePort.update(id, et);
        dispatchEvents(et);
        return et;
    }

    @Override
    public EquipmentType addMetrologicalData(String equipmentTypeId, MetrologicalDataCommand command) {
        EquipmentType et = equipmentTypePersistencePort.findById(equipmentTypeId)
                .orElseThrow(() -> new EquipmentTypeNotFoundException(equipmentTypeId));

        MetrologicalData md = MetrologicalData.builder()
                .value(command.value())
                .type(command.type())
                .build();
        et.addMetrologicalData(md);
        equipmentTypePersistencePort.addMetrologicalData(equipmentTypeId, md);
        dispatchEvents(et);
        return et;
    }

    @Override
    public EquipmentType removeMetrologicalData(String equipmentTypeId, MetrologicalDataCommand command) {
        EquipmentType et = equipmentTypePersistencePort.findById(equipmentTypeId)
                .orElseThrow(() -> new EquipmentTypeNotFoundException(equipmentTypeId));

        MetrologicalData md = MetrologicalData.builder()
                .value(command.value())
                .type(command.type())
                .build();

        System.out.println(command);
        System.out.println(md.toString());
        et.removeMetrologicalData(md);
        equipmentTypePersistencePort.removeMetrologicalData(equipmentTypeId, md);
        dispatchEvents(et);
        return et;
    }

    @Override
    public EquipmentType updateMetrologicalData(String equipmentTypeId,
                                                MetrologicalDataCommand oldCommand,
                                                MetrologicalDataCommand newCommand) {
        EquipmentType et = equipmentTypePersistencePort.findById(equipmentTypeId)
                .orElseThrow(() -> new EquipmentTypeNotFoundException(equipmentTypeId));

        MetrologicalData oldMd = MetrologicalData.builder()
                .value(oldCommand.value())
                .type(oldCommand.type())
                .build();
        MetrologicalData newMd = MetrologicalData.builder()
                .value(newCommand.value())
                .type(newCommand.type())
                .build();
        et.updateMetrologicalData(oldMd, newMd);
        equipmentTypePersistencePort.removeMetrologicalData(equipmentTypeId, oldMd);
        equipmentTypePersistencePort.addMetrologicalData(equipmentTypeId, newMd);
        dispatchEvents(et);
        return et;
    }

    @Override
    public EquipmentType addMetrologicalDataList(String equipmentTypeId,
                                                 List<MetrologicalDataCommand> command) {
        EquipmentType et = equipmentTypePersistencePort.findById(equipmentTypeId)
                .orElseThrow(() -> new EquipmentTypeNotFoundException(equipmentTypeId));

        List<MetrologicalData> mdList = new ArrayList<>();
        for(MetrologicalDataCommand c: command) {
            MetrologicalData md = MetrologicalData.builder()
                    .value(c.value())
                    .type(c.type())
                    .build();
            et.addMetrologicalData(md);
            mdList.add(md);
        }

        equipmentTypePersistencePort.addMetrologicalDataList(equipmentTypeId, mdList);
        dispatchEvents(et);
        return et;
    }

    @Override
    public EquipmentType removeMetrologicalDataList(String equipmentTypeId, List<MetrologicalDataCommand> command) {
        EquipmentType et = equipmentTypePersistencePort.findById(equipmentTypeId)
                .orElseThrow(() -> new EquipmentTypeNotFoundException(equipmentTypeId));

        List<MetrologicalData> mdList = new ArrayList<>();
        for(MetrologicalDataCommand c: command) {
            MetrologicalData md = MetrologicalData.builder()
                    .value(c.value())
                    .type(c.type())
                    .build();
            et.removeMetrologicalData(md);
            mdList.add(md);
        }

        equipmentTypePersistencePort.removeMetrologicalDataList(equipmentTypeId, mdList);
        dispatchEvents(et);
        return et;
    }

    @Override
    public EquipmentType updateMetrologicalDataList(String equipmentTypeId,
                                                    List<MetrologicalDataCommand> oldCommand,
                                                    List<MetrologicalDataCommand> newCommand) {


        EquipmentType et = equipmentTypePersistencePort.findById(equipmentTypeId)
                .orElseThrow(() -> new EquipmentTypeNotFoundException(equipmentTypeId));

        if(oldCommand.size() != newCommand.size()) {
            throw new RuntimeException("Number of old commands does not match number of new commands");
        }

        List<MetrologicalData> oldMdList = new ArrayList<>();
        List<MetrologicalData> newMdList = new ArrayList<>();

        for(int i = 0; i < oldCommand.size(); i++) {
            MetrologicalDataCommand oldC = oldCommand.get(i);
            MetrologicalDataCommand newC = newCommand.get(i);
            MetrologicalData oldMd = MetrologicalData.builder()
                    .value(oldC.value())
                    .type(oldC.type())
                    .build();
            MetrologicalData newMd = MetrologicalData.builder()
                    .value(newC.value())
                    .type(newC.type())
                    .build();
            et.updateMetrologicalData(oldMd, newMd);
            oldMdList.add(oldMd);
            newMdList.add(newMd);
        }

        equipmentTypePersistencePort.removeMetrologicalDataList(equipmentTypeId, oldMdList);
        equipmentTypePersistencePort.addMetrologicalDataList(equipmentTypeId, newMdList);
        dispatchEvents(et);
        return et;
    }

    @Override
    public EquipmentType addTechicalVerificationId(String equipmentTypeId, UUID technicalVerificationId) {
        EquipmentType et = equipmentTypePersistencePort.findById(equipmentTypeId)
                .orElseThrow(() -> new EquipmentTypeNotFoundException(equipmentTypeId));

        et.addTechicalVerificationId(technicalVerificationId);
        equipmentTypePersistencePort.addTechnicalVerification(equipmentTypeId, technicalVerificationId);
        dispatchEvents(et);
        return et;
    }

    @Override
    public EquipmentType removeTechicalVerificationId(String equipmentTypeId, UUID technicalVerificationId) {
        EquipmentType et = equipmentTypePersistencePort.findById(equipmentTypeId)
                .orElseThrow(() -> new EquipmentTypeNotFoundException(equipmentTypeId));

        et.removeTechicalVerificationId(technicalVerificationId);
        equipmentTypePersistencePort.removeTechnicalVerification(equipmentTypeId, technicalVerificationId);
        dispatchEvents(et);
        return et;
    }

    @Override
    public EquipmentType updateTechicalVerificationId(String equipmentTypeId,
                                                      UUID oldTechnicalVerificationId,
                                                      UUID newTechnicalVerificationId) {
        EquipmentType et = equipmentTypePersistencePort.findById(equipmentTypeId)
                .orElseThrow(() -> new EquipmentTypeNotFoundException(equipmentTypeId));

        et.updateTechicalVerificationId(oldTechnicalVerificationId, newTechnicalVerificationId);
        equipmentTypePersistencePort.removeTechnicalVerification(equipmentTypeId, oldTechnicalVerificationId);
        equipmentTypePersistencePort.addTechnicalVerification(equipmentTypeId, newTechnicalVerificationId);
        dispatchEvents(et);
        return et;
    }

    @Override
    public EquipmentType addTechicalVerificationIdList(String equipmentTypeId, Set<UUID> technicalVerificationIds) {
        EquipmentType et = equipmentTypePersistencePort.findById(equipmentTypeId)
                .orElseThrow(() -> new EquipmentTypeNotFoundException(equipmentTypeId));

        for (UUID id : technicalVerificationIds) {
            et.addTechicalVerificationId(id);
        }

        equipmentTypePersistencePort.addTechnicalVerificationList(equipmentTypeId, technicalVerificationIds);
        dispatchEvents(et);
        return et;
    }

    @Override
    public EquipmentType removeTechicalVerificationIdList(String equipmentTypeId, Set<UUID> technicalVerificationIds) {
        EquipmentType et = equipmentTypePersistencePort.findById(equipmentTypeId)
                .orElseThrow(() -> new EquipmentTypeNotFoundException(equipmentTypeId));

        for (UUID id : technicalVerificationIds) {
            et.removeTechicalVerificationId(id);
        }

        equipmentTypePersistencePort.removeTechnicalVerificationList(equipmentTypeId, technicalVerificationIds);
        dispatchEvents(et);
        return et;
    }

    @Override
    public EquipmentType updateTechicalVerificationIdList(String equipmentTypeId,
                                                          Set<UUID> oldTechnicalVerificationIds,
                                                          Set<UUID> newTechnicalVerificationIds) {
        EquipmentType et = equipmentTypePersistencePort.findById(equipmentTypeId)
                .orElseThrow(() -> new EquipmentTypeNotFoundException(equipmentTypeId));

        for (UUID id : oldTechnicalVerificationIds) {
            et.removeTechicalVerificationId(id);
        }

        for (UUID id : newTechnicalVerificationIds) {
            et.addTechicalVerificationId(id);
        }

        equipmentTypePersistencePort.removeTechnicalVerificationList(equipmentTypeId, oldTechnicalVerificationIds);
        equipmentTypePersistencePort.addTechnicalVerificationList(equipmentTypeId, newTechnicalVerificationIds);
        dispatchEvents(et);
        return et;
    }

    private void dispatchEvents(EquipmentType aggregate) {
        List<DomainEvent<? extends Payload>> events = aggregate.pullEvents();
        events.forEach(eventDispatcherPort::dispatch);
    }
}
