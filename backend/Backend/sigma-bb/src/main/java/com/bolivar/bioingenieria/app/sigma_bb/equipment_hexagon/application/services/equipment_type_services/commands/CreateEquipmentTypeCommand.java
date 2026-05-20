package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.equipment_type_services.commands;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.metrological_data.MetrologicalData;

import java.math.BigDecimal;
import java.util.List;

public record CreateEquipmentTypeCommand(
        String equipmentTypeName,
        String technicalDefinition,
        String careRecommendations,
        Integer voltage,
        BigDecimal amperage,
        String predominantTechnology,
        Boolean verifiable,
        Long unitMaintenanceValue,
        List<MetrologicalData> metrologicalData
) {
}
