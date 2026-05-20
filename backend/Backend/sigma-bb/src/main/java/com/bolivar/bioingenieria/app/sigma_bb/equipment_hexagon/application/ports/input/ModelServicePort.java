package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.ports.input;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.model_services.commands.CreateModelCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.model_services.commands.DeleteModelCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.model_services.commands.UpdateModelCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.model.Model;

import java.util.List;

public interface ModelServicePort {
    List<Model> findAll();
    Model findById(String id);
    Model save(CreateModelCommand createModelCommand);
    Model update(String id, UpdateModelCommand updateModelCommand);
    void delete(DeleteModelCommand deleteModelCommand);
}
