package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.ports.output;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.equipment_type.EquipmentType;

import java.util.List;
import java.util.Optional;

public interface EquipmentTypePersistencePort {
    List<EquipmentType> findAll();
    Optional<EquipmentType> findById(String id);
    EquipmentType save(EquipmentType equipmentType);
    EquipmentType update(String id, EquipmentType equipmentType);
    void delete(String id);


}
