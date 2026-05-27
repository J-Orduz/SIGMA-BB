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
@Schema(name = "TechnicalVerificationPatchRequest", description = "DTO de entrada para actualizar parcialmente una verificación técnica de equipo.")
public class TechnicalVerificationPatchRequest {
    @Schema(description = "Descripción de la verificación técnica", example = "Verificación de calibración de electrodos", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String description;

    @Schema(description = "Tipo de verificación a realizar", example = "Calibración", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String verificationType;
}
