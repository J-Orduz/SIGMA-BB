package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "EquipmentTypePatchRequest", description = "DTO de entrada para actualizar parcialmente un tipo de equipo biomédico. Solo los campos enviados serán modificados.")
public class EquipmentTypePatchRequest {
    @Schema(description = "Nombre del tipo de equipo", example = "Electrocardiógrafo", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String equipmentTypeName;

    @Schema(description = "Definición técnica del equipo", example = "Equipo para registro de actividad eléctrica del corazón", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String technicalDefinition;

    @Schema(description = "Recomendaciones de cuidado y mantenimiento", example = "Limpiar electrodos después de cada uso", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String careRecommendations;

    @Schema(description = "Voltaje de operación en voltios", example = "110", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer voltage;

    @Schema(description = "Amperaje de operación", example = "2.5", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private BigDecimal amperage;

    @Schema(description = "Tecnología predominante del equipo", example = "Digital", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String predominantTechnology;

    @Schema(description = "Indica si el equipo requiere verificación técnica periódica", example = "true", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Boolean verifiable;

    @Schema(description = "Valor unitario de mantenimiento en pesos", example = "150000", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long unitMaintenanceValue;
}
