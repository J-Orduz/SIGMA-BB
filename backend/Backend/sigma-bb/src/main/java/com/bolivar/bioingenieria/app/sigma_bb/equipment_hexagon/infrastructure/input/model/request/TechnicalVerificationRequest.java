package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "TechnicalVerificationRequest", description = "DTO de entrada para crear o actualizar una verificación técnica de equipo.")
public class TechnicalVerificationRequest {
    @Schema(description = "Descripción de la verificación técnica", example = "Verificación de calibración de electrodos", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "La descripción es obligatoria")
    private String description;

    @Schema(description = "Tipo de verificación a realizar", example = "Calibración", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El tipo de verificación es obligatorio")
    private String verificationType;
}
