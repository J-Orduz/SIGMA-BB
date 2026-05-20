package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.mapper;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.equipment.Equipment;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.model.request.EquipmentRequest;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.model.response.EquipmentReponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        uses = {EquipmentTypeRestMapper.class, BrandRestMapper.class})
public interface EquipmentRestMapper {
    Equipment toEquipment(EquipmentRequest equipmentRequest);

    @Mapping(target = "equipmentTypeResponse", source = "equipmentType")
    @Mapping(target = "brandResponse", source = "brand")
    EquipmentReponse toEquipmentResponse(Equipment equipment);

    List<EquipmentReponse> toEquipmentResponseList(List<Equipment> equipmentList);
}
