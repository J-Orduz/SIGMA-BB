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
@Schema(name = "CountryPatchRequest", description = "DTO de entrada para actualizar parcialmente un país.")
public class CountryPatchRequest {
    @Schema(description = "Identificador único del país", example = "COL", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String id;

    @Schema(description = "Nombre del país", example = "Colombia", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String name;
}
