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
@Schema(name = "ManufacturerRequest", description = "DTO de entrada para crear o actualizar un fabricante.")
public class ManufacturerRequest {
    @Schema(description = "Nombre del fabricante", example = "GE Healthcare", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El nombre del fabricante es obligatorio")
    private String name;

    @Schema(description = "Identificador del país del fabricante", example = "COL", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El id del país es obligatorio")
    private String countryId;
}
