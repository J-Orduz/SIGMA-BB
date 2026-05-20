package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UpdateMetrologicalDataBody", description = "DTO de entrada para actualizar un dato metrológico individual, reemplazando el valor anterior por el nuevo.")
public class UpdateMetrologicalDataBody {

    @Schema(description = "Dato metrológico a reemplazar", requiredMode = Schema.RequiredMode.REQUIRED)
    @Valid
    @NotNull
    private MetrologicalDataRequest oldData;

    @Schema(description = "Nuevo dato metrológico", requiredMode = Schema.RequiredMode.REQUIRED)
    @Valid
    @NotNull
    private MetrologicalDataRequest newData;
}
