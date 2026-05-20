package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.equipment_type.events;

import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.Payload;

import java.math.BigDecimal;

public record EquipmentTypePayload(
        String equipmentTypeName, String technicalDefinition, String careRecommendations,
        Integer voltage, BigDecimal amperage, String predominantTechnology,
        Boolean verifiable, Long unitMaintenanceValue
) implements Payload {}
