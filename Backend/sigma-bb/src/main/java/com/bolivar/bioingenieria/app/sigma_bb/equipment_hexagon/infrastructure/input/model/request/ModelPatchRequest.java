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
@Schema(name = "ModelPatchRequest", description = "DTO de entrada para actualizar parcialmente un modelo de equipo.")
public class ModelPatchRequest {

    @Schema(description = "Registro INVIMA del modelo", example = "INVIMA-2024-001", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String invima;

    @Schema(description = "Identificador del fabricante del modelo", example = "550e8400-e29b-41d4-a716-446655440000", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String manufacturerId;

    @Schema(description = "Identificador del tipo de equipo asociado al modelo", example = "660e8400-e29b-41d4-a716-446655440001", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String equipmentId;
}
