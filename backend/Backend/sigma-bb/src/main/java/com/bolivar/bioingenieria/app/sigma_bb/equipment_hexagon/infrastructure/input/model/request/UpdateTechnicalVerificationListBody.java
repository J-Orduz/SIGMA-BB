package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UpdateTechnicalVerificationListBody", description = "DTO de entrada para actualizar múltiples verificaciones técnicas en lote, reemplazando los IDs anteriores por los nuevos.")
public class UpdateTechnicalVerificationListBody {

    @Schema(description = "Lista de IDs de verificaciones técnicas a reemplazar", requiredMode = Schema.RequiredMode.REQUIRED)
    @Valid
    @NotNull
    private Set<UUID> oldTechnicalVerificationIds;

    @Schema(description = "Lista de nuevos IDs de verificaciones técnicas", requiredMode = Schema.RequiredMode.REQUIRED)
    @Valid
    @NotNull
    private Set<UUID> newTechnicalVerificationIds;
}
