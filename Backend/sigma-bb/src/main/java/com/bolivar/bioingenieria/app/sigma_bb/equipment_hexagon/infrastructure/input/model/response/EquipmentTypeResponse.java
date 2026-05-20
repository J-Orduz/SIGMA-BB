package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentTypeResponse {
    private UUID id;
    private String equipmentTypeName;
    private String technicalDefinition;
    private String careRecommendations;
    private Integer voltage;
    private BigDecimal amperage;
    private String predominantTechnology;
    private Boolean verifiable;
    private Long unitMaintenanceValue;
    private List<MetrologicalDataResponse> metrologicalData;
    private Set<UUID> technicalVerification;
}
