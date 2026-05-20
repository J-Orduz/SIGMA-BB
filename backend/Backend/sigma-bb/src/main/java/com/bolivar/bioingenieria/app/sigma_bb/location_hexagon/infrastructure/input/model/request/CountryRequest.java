package com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.infrastructure.input.model.request;

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
@Schema(name = "CountryRequest", description = "DTO de entrada para crear o actualizar un país.")
public class CountryRequest {
    @Schema(description = "Identificador único del país", example = "COL", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El id del país es obligatorio")
    private String id;

    @Schema(description = "Nombre del país", example = "Colombia", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El nombre del país es obligatorio")
    private String name;
}
