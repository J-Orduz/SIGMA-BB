package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UpdateTechnicalVerificationBody", description = "DTO de entrada para actualizar una verificación técnica individual, reemplazando el ID anterior por el nuevo.")
public class UpdateTechnicalVerificationBody {

    @Schema(description = "ID de la verificación técnica a reemplazar", example = "550e8400-e29b-41d4-a716-446655440000", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private UUID oldTechnicalVerificationId;

    @Schema(description = "Nuevo ID de verificación técnica", example = "660e8400-e29b-41d4-a716-446655440001", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private UUID newTechnicalVerificationId;
}
