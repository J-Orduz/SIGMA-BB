package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.ports.input.EquipmentServicePort;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.ports.output.BrandPersistencePort;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.ports.output.EquipmentPersistencePort;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.ports.output.EquipmentTypePersistencePort;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.brand.Brand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.equipment.Equipment;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.equipment_type.EquipmentType;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.errors.BrandNotFoundException;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.errors.EquipmentNotFoundException;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.errors.EquipmentTypeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EquipmentQueryService implements EquipmentServicePort {
    private final EquipmentPersistencePort persistencePort;
    private final EquipmentTypePersistencePort equipmentTypePersistencePort;
    private final BrandPersistencePort brandPersistencePort;

    @Autowired
    public EquipmentQueryService(EquipmentPersistencePort persistencePort,
                                 EquipmentTypePersistencePort equipmentTypePersistencePort,
                                 BrandPersistencePort brandPersistencePort) {
        this.persistencePort = persistencePort;
        this.equipmentTypePersistencePort = equipmentTypePersistencePort;
        this.brandPersistencePort = brandPersistencePort;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Equipment> findAll() {
        return persistencePort.findAll().stream().map(this::attachRelations).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Equipment findById(String id) {
        Equipment equipment = persistencePort.findById(id)
                .orElseThrow(() -> new EquipmentNotFoundException(id));
        return attachRelations(equipment);
    }

    private Equipment attachRelations(Equipment equipment) {
        if (equipment.getEquipmentTypeId() != null) {
            EquipmentType equipmentType = equipmentTypePersistencePort
                    .findById(equipment.getEquipmentTypeId().toString())
                    .orElseThrow(() -> new EquipmentTypeNotFoundException(equipment.getEquipmentTypeId().toString()));
            equipment.setEquipmentType(equipmentType);
        }
        if (equipment.getBrandId() != null) {
            Brand brand = brandPersistencePort
                    .findById(equipment.getBrandId().toString())
                    .orElseThrow(() -> new BrandNotFoundException(equipment.getBrandId().toString()));
            equipment.setBrand(brand);
        }
        return equipment;
    }
}

