package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.ports.input.ModelServicePort;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.ports.output.ModelPersistencePort;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.model_services.commands.CreateModelCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.model_services.commands.DeleteModelCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.model_services.commands.UpdateModelCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.model.Model;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.errors.ModelNotFoundException;
import com.bolivar.bioingenieria.app.sigma_bb.shared.application.ports.output.EventDispatcherPort;
import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.DomainEvent;
import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.Payload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModelService implements ModelServicePort {
    private final ModelPersistencePort modelPersistencePort;
    private final EventDispatcherPort eventDispatcherPort;

    @Autowired
    public ModelService(ModelPersistencePort modelPersistencePort,
                        @Qualifier(value = "springDispatcher") EventDispatcherPort eventDispatcherPort) {
        this.modelPersistencePort = modelPersistencePort;
        this.eventDispatcherPort = eventDispatcherPort;
    }

    @Override
    public List<Model> findAll() {
        return modelPersistencePort.findAll();
    }

    @Override
    public Model findById(String id) {
        return this.modelPersistencePort.findById(id)
                .orElseThrow(() -> new ModelNotFoundException(id));
    }

    @Override
    public Model save(CreateModelCommand command) {
        Model model = Model.create(command.invima(), command.manufacturerId(), command.equipmentId());
        modelPersistencePort.save(model);
        dispatchEvents(model);
        return model;
    }

    @Override
    public Model update(String id, UpdateModelCommand command) {
        Model model = modelPersistencePort.findById(id)
                .orElseThrow(() -> new ModelNotFoundException(id));
        model.updateModel(command.invima(), command.manufacturerId(), command.equipmentId());
        modelPersistencePort.update(id, model);
        dispatchEvents(model);
        return model;
    }

    @Override
    public void delete(DeleteModelCommand command) {
        Model model = modelPersistencePort.findById(command.id())
                .orElseThrow(() -> new ModelNotFoundException(command.id()));
        model.deleteModel();
        modelPersistencePort.delete(command.id());
        dispatchEvents(model);
    }

    private void dispatchEvents(Model aggregate) {
        List<DomainEvent<? extends Payload>> events = aggregate.pullEvents();
        events.forEach(eventDispatcherPort::dispatch);
    }
}
