package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.equipment_type_services.commands;

import java.math.BigDecimal;

public record EquipmentTypePatchCommand(
        String equipmentTypeName,
        String technicalDefinition,
        String careRecommendations,
        Integer voltage,
        BigDecimal amperage,
        String predominantTechnology,
        Boolean verifiable,
        Long unitMaintenanceValue
) {
}
