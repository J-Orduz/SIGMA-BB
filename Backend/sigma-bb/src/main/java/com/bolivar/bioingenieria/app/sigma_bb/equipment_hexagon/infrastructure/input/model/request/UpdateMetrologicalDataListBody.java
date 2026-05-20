package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UpdateMetrologicalDataListBody", description = "DTO de entrada para actualizar múltiples datos metrológicos en lote, reemplazando los valores anteriores por los nuevos.")
public class UpdateMetrologicalDataListBody {

    @Schema(description = "Lista de datos metrológicos a reemplazar", requiredMode = Schema.RequiredMode.REQUIRED)
    @Valid
    @NotNull
    private List<MetrologicalDataRequest> oldData;

    @Schema(description = "Lista de nuevos datos metrológicos", requiredMode = Schema.RequiredMode.REQUIRED)
    @Valid
    @NotNull
    private List<MetrologicalDataRequest> newData;
}
