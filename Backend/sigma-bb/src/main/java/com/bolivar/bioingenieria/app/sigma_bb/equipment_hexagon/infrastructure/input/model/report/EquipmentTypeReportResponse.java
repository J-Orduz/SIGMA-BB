package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.model.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentTypeReportResponse {
    private UUID id;
    private String equipmentTypeName;
    private String technicalDefinition;
    private String careRecommendations;
    private Integer voltage;
    private BigDecimal amperage;
    private String predominantTechnology;
    private Boolean verifiable;
    private Long unitMaintenanceValue;
    private List<MetrologicalDataReportResponse> metrologicalData;
}
