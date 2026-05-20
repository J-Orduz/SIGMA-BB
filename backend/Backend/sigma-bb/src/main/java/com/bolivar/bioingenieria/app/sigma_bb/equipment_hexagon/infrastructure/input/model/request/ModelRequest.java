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
@Schema(name = "ModelRequest", description = "DTO de entrada para crear o actualizar un modelo de equipo.")
public class ModelRequest {

    @Schema(description = "Registro INVIMA del modelo", example = "INVIMA-2024-001", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El invima del modelo es obligatorio")
    private String invima;

    @Schema(description = "Identificador del fabricante del modelo", example = "550e8400-e29b-41d4-a716-446655440000", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El id del fabricante es obligatorio")
    private String manufacturerId;

    @Schema(description = "Identificador del tipo de equipo asociado al modelo", example = "660e8400-e29b-41d4-a716-446655440001", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El id del equipo es obligatorio")
    private String equipmentId;
}
