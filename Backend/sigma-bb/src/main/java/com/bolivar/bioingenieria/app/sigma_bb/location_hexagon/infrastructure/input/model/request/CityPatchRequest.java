package com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.infrastructure.input.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "CityPatchRequest", description = "DTO de entrada para actualizar parcialmente una ciudad.")
public class CityPatchRequest {
    @Schema(description = "Identificador único de la ciudad", example = "BOG", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String id;

    @Schema(description = "Nombre de la ciudad", example = "Bogotá", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String name;

    @Schema(description = "Identificador del país al que pertenece la ciudad", example = "COL", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String countryId;
}
