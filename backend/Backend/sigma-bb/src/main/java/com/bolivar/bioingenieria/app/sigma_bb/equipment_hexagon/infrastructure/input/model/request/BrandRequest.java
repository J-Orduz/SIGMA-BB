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
@Schema(name = "BrandRequest", description = "DTO de entrada para crear o actualizar una marca.")
public class BrandRequest {
    @Schema(description = "Nombre de la marca", example = "Siemens", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El nombre de la marca es obligatorio")
    private String name;
}
