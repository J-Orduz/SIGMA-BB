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
@Schema(name = "CityRequest", description = "DTO de entrada para crear o actualizar una ciudad.")
public class CityRequest {
    @Schema(description = "Identificador único de la ciudad", example = "BOG", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El id de la ciudad es obligatorio")
    private String id;

    @Schema(description = "Nombre de la ciudad", example = "Bogotá", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El nombre de la ciudad es obligatorio")
    private String name;

    @Schema(description = "Identificador del país al que pertenece la ciudad", example = "COL", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El id del país es obligatorio")
    private String countryId;
}
