package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "EquipmentPatchRequest", description = "DTO de entrada para actualizar parcialmente un equipo.")
public class EquipmentPatchRequest {
    @Schema(description = "Identificador del tipo de equipo", example = "550e8400-e29b-41d4-a716-446655440000", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String equipmentTypeId;

    @Schema(description = "Identificador de la marca del equipo", example = "550e8400-e29b-41d4-a716-446655440001", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String brandId;
}
