package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.ports.input;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.equipment_services.commands.EquipmentPatchCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.equipment_services.commands.CreateEquipmentCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.equipment_services.commands.DeleteEquipmentCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.equipment_services.commands.UpdateEquipmentCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.equipment.Equipment;

import java.util.List;

public interface EquipmentServicePort {
    List<Equipment> findAll();
    Equipment findById(String id);
    Equipment save(CreateEquipmentCommand command);
    Equipment update(String id, UpdateEquipmentCommand command);
    void delete(DeleteEquipmentCommand command);
    Equipment patchUpdate(String id, EquipmentPatchCommand command);
}
