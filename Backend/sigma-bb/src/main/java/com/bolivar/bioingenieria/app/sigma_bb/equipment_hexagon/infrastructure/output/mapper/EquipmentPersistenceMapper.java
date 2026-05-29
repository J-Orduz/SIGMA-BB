package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.mapper;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.equipment.Equipment;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.entities.EquipmentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface EquipmentPersistenceMapper {

    @Mapping(target = "equipmentType", ignore = true)
    @Mapping(target = "brand", ignore = true)
    Equipment toEquipment(EquipmentEntity equipmentEntity);

    EquipmentEntity toEquipmentEntity(Equipment equipment);

    List<Equipment> toEquipmentList(List<EquipmentEntity> equipmentEntities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    void updateEntityFromDomain(Equipment source, @MappingTarget EquipmentEntity target);
}
