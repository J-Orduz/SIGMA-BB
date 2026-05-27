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
@Schema(name = "BrandPatchRequest", description = "DTO de entrada para actualizar parcialmente una marca.")
public class BrandPatchRequest {
    @Schema(description = "Nombre de la marca", example = "Siemens", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String name;
}
