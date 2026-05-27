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
@Schema(name = "ManufacturerPatchRequest", description = "DTO de entrada para actualizar parcialmente un fabricante.")
public class ManufacturerPatchRequest {
    @Schema(description = "Nombre del fabricante", example = "GE Healthcare", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String name;

    @Schema(description = "Identificador del país del fabricante", example = "COL", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String countryId;
}
