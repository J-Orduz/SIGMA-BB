package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.ports.input;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.equipment_type_services.commands.EquipmentTypePatchCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.equipment_type_services.commands.CreateEquipmentTypeCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.equipment_type_services.commands.DeleteEquipmentTypeCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.equipment_type_services.commands.UpdateEquipmentTypeCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.metrological_data_services.commands.MetrologicalDataCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.equipment_type.EquipmentType;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface EquipmentTypeServicePort {
    List<EquipmentType> findAll();
    EquipmentType findById(String id);
    EquipmentType save(CreateEquipmentTypeCommand command);
    EquipmentType update(String id, UpdateEquipmentTypeCommand command);
    void delete(DeleteEquipmentTypeCommand command);
    EquipmentType patchUpdate(String id, EquipmentTypePatchCommand command);

    EquipmentType addMetrologicalData(String  equipmentTypeId,
                                             MetrologicalDataCommand command);

    EquipmentType removeMetrologicalData(String  equipmentTypeId,
                                                MetrologicalDataCommand command);

    EquipmentType updateMetrologicalData(String equipmentTypeId,
                                         MetrologicalDataCommand oldCommand,
                                         MetrologicalDataCommand newCommand);

    EquipmentType addMetrologicalDataList(String  equipmentTypeId,
                                                 List<MetrologicalDataCommand> command );

    EquipmentType removeMetrologicalDataList(String  equipmentTypeId,
                                                    List<MetrologicalDataCommand> command);

    EquipmentType updateMetrologicalDataList(String  equipmentTypeId,
                                             List<MetrologicalDataCommand> oldCommand
                                             ,List<MetrologicalDataCommand> newCommand);

    EquipmentType addTechicalVerificationId(String equipmentTypeId, UUID technicalVerificationId);

    EquipmentType removeTechicalVerificationId(String equipmentTypeId, UUID technicalVerificationId);

    EquipmentType updateTechicalVerificationId(String equipmentTypeId, UUID oldTechnicalVerificationId, UUID newTechnicalVerificationId);

    EquipmentType addTechicalVerificationIdList(String equipmentTypeId, Set<UUID> technicalVerificationIds);

    EquipmentType removeTechicalVerificationIdList(String equipmentTypeId, Set<UUID> technicalVerificationIds);

    EquipmentType updateTechicalVerificationIdList(String equipmentTypeId,
                                                   Set<UUID> oldTechnicalVerificationIds,
                                                   Set<UUID> newTechnicalVerificationIds);
}
