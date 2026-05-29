package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.ports.output;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.equipment_type.EquipmentType;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.metrological_data.MetrologicalData;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface EquipmentTypePersistencePort {
    boolean exists(UUID id);

    List<EquipmentType> findAll();
    Optional<EquipmentType> findById(String id);
    EquipmentType save(EquipmentType equipmentType);
    EquipmentType update(String id, EquipmentType equipmentType);
    void delete(String id);

    void addMetrologicalData(String equipmentTypeId, MetrologicalData data);
    void removeMetrologicalData(String equipmentTypeId, MetrologicalData data);
    void addMetrologicalDataList(String equipmentTypeId, List<MetrologicalData> data);
    void removeMetrologicalDataList(String equipmentTypeId, List<MetrologicalData> data);

    void addTechnicalVerification(String equipmentTypeId, UUID verificationId);
    void removeTechnicalVerification(String equipmentTypeId, UUID verificationId);
    void addTechnicalVerificationList(String equipmentTypeId, Set<UUID> ids);
    void removeTechnicalVerificationList(String equipmentTypeId, Set<UUID> ids);
}
