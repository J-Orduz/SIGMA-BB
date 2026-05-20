package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "MetrologicalDataRequest", description = "DTO de entrada para un dato metrológico asociado a un tipo de equipo.")
public class MetrologicalDataRequest {
    @Schema(description = "Valor de la medición metrológica", example = "120.5", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El valor es obligatorio")
    private BigDecimal value;

    @Schema(description = "Tipo de dato metrológico", example = "Voltaje", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El tipo es obligatorio")
    private String type;
}
