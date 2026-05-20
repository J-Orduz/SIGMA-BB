package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "EquipmentTypeRequest", description = "DTO de entrada para crear o actualizar un tipo de equipo biomédico, incluyendo sus especificaciones técnicas y datos metrológicos.")
public class EquipmentTypeRequest {
    @Schema(description = "Nombre del tipo de equipo", example = "Electrocardiógrafo", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El nombre del tipo de equipo es obligatorio")
    private String equipmentTypeName;

    @Schema(description = "Definición técnica del equipo", example = "Equipo para registro de actividad eléctrica del corazón", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "La definición técnica es obligatoria")
    private String technicalDefinition;

    @Schema(description = "Recomendaciones de cuidado y mantenimiento", example = "Limpiar electrodos después de cada uso", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Las recomendaciones de cuidado son obligatorias")
    private String careRecommendations;

    @Schema(description = "Voltaje de operación en voltios", example = "110", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El voltage es obligatorio")
    private Integer voltage;

    @Schema(description = "Amperaje de operación", example = "2.5", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El amperaje es obligatorio")
    private BigDecimal amperage;

    @Schema(description = "Tecnología predominante del equipo", example = "Digital", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "La tecnología predominante es obligatoria")
    private String predominantTechnology;

    @Schema(description = "Indica si el equipo requiere verificación técnica periódica", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El campo verificable es obligatorio")
    private Boolean verifiable;

    @Schema(description = "Valor unitario de mantenimiento en pesos", example = "150000", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El valor unitario de mantenimiento es obligatorio")
    private Long unitMaintenanceValue;

    @Schema(description = "Lista de datos metrológicos asociados al tipo de equipo", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<MetrologicalDataRequest> metrologicalData;
}
