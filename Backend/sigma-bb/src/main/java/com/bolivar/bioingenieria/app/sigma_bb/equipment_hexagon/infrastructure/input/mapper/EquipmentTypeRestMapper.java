package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.mapper;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.equipment_type.EquipmentType;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.model.request.EquipmentTypeRequest;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.model.response.EquipmentTypeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        uses = {MetrologicalDataRestMapper.class})
public interface EquipmentTypeRestMapper {
    EquipmentType toEquipmentType(EquipmentTypeRequest equipmentTypeRequest);
    EquipmentTypeResponse toEquipmentTypeResponse(EquipmentType equipmentType);
    List<EquipmentTypeResponse> toEquipmentTypeResponseList(List<EquipmentType> equipmentTypeList);

}
