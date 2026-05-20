package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentReponse {
    private UUID id;
    private UUID equipmentTypeId;
    private UUID brandId;
    private BrandResponse brandResponse;
    private EquipmentTypeResponse equipmentTypeResponse;
}
