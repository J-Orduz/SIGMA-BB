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
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {EquipmentTypePersistenceMapper.class, BrandPersistenceMapper.class}
)
public interface EquipmentPersistenceMapper {

    @Mapping(target = "equipmentTypeId", source = "equipmentType.id")
    @Mapping(target = "brandId", source = "brand.id")
    @Mapping(target = "equipmentType", source="equipmentType")
    @Mapping(target = "brand", source="brand")
    Equipment toEquipment(EquipmentEntity equipmentEntity);

    @Mapping(target = "equipmentType.id", source = "equipmentTypeId")
    @Mapping(target = "brand.id", source = "brandId")
    EquipmentEntity toEquipmentEntity(Equipment equipment);

    List<Equipment> toEquipmentList(List<EquipmentEntity> equipmentEntities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "equipmentType", ignore = true)
    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "active", ignore = true)
    void updateEntityFromDomain(Equipment source, @MappingTarget EquipmentEntity target);
}
